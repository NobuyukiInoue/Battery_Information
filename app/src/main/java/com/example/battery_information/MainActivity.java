package com.example.battery_information;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.TextView;


//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {
    private StringBuilder sbuilder = new StringBuilder();
    private Intent batteryStatus;
    private BatteryManager bManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        batteryStatus = this.registerReceiver(null, ifilter);

        if(batteryStatus != null) {
            baInfo();
        }

        // 以降は Lollipop と機種によるサポートの違いあり
        if(Build.VERSION.SDK_INT >= 21) {
            bManager = (BatteryManager)this.getSystemService(Context.BATTERY_SERVICE);

            if(bManager != null){
                batteryAgeing();
            }
        }

        TextView textView = findViewById(R.id.text_view);
        textView.setText(sbuilder);

    }

    private void baInfo(){

        // Battery Level
        int level = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1);
        sbuilder.append("Level: ");
        sbuilder.append(String.valueOf(level));
        sbuilder.append("\n");

        // Battery Health
        int bh = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_HEALTH, -1);
        sbuilder.append("Health: ");
        sbuilder.append(batteryHealth(bh));
        sbuilder.append("\n");

        // icon ID
        int bi = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_ICON_SMALL, -1);
        sbuilder.append("Icon: ");
        sbuilder.append(String.valueOf(bi));
        sbuilder.append("\n");

        // Battery plugged
        int bpl = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_PLUGGED, -1);
        sbuilder.append("Plugged: ");
        sbuilder.append(batteryPluggCheck(bpl));
        sbuilder.append("\n");

        // Battery present or not
        boolean bpr = batteryStatus.getBooleanExtra(
                BatteryManager.EXTRA_PRESENT, false);
        sbuilder.append("Present: ");
        sbuilder.append(batteryPresent(bpr));
        sbuilder.append("\n");

        // scale
        int bsc = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1);
        sbuilder.append("Scale: ");
        sbuilder.append(String.valueOf(bsc));
        sbuilder.append("\n");

        // Battery status
        int bst = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_STATUS, -1);
        sbuilder.append("Status: ");
        sbuilder.append(status(bst));
        sbuilder.append("\n");

        // Battery technology
        String bte = batteryStatus.getStringExtra(
                BatteryManager.EXTRA_TECHNOLOGY);
        sbuilder.append("Technology: ");
        sbuilder.append(bte);
        sbuilder.append("\n");

        // temperature
        int btp = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, -1);
        sbuilder.append("Tempareture: ");
        sbuilder.append(String.valueOf((float)btp/10));
        sbuilder.append(" ℃\n");

        // Voltage
        int bv = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_VOLTAGE, -1);
        sbuilder.append("Voltage: ");
        sbuilder.append(String.valueOf((float)bv/1000));
        sbuilder.append(" [v]\n\n");

    }

    private String batteryHealth(int bh){
        String health = null;

        if(bh == BatteryManager.BATTERY_HEALTH_GOOD){
            health = "GOOD";
        }
        else if(bh == BatteryManager.BATTERY_HEALTH_DEAD){
            health = "DEAD";
        }
        else if(bh == BatteryManager.BATTERY_HEALTH_COLD){
            health = "COLD";
        }
        else if(bh == BatteryManager.BATTERY_HEALTH_OVERHEAT){
            health = "OVERHEAT";
        }
        else if(bh == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
            health = "OVER_VOLTAGE";
        }
        else if(bh == BatteryManager.BATTERY_HEALTH_UNKNOWN){
            health = "UNKNOWN";
        }
        else if(bh == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
            health = "UNSPECIFIED_FAILURE";
        }

        return health;
    }

    private String batteryPluggCheck(int bpl){
        String plugged = "NOT PLUGGED";
        if(bpl == BatteryManager.BATTERY_PLUGGED_AC){
            plugged = "PLUGGED AC";
        }
        else if(bpl == BatteryManager.BATTERY_PLUGGED_USB){
            plugged = "PLUGGED USB";
        }
        else if(bpl == BatteryManager.BATTERY_PLUGGED_WIRELESS){
            plugged = "PLUGGED WIRELESS";
        }

        return plugged;
    }

    private String batteryPresent(boolean bpr){
        String present;
        if(bpr){
            present = "PRESENT";
        }
        else{
            present = "NOT PRESENT";
        }
        return present;
    }

    private String status(int bst){
        String status = "?";
        if(bst == BatteryManager.BATTERY_STATUS_CHARGING){
            status = "CHARGING";
        }
        else if(bst == BatteryManager.BATTERY_STATUS_DISCHARGING){
            status = "DISCHARGING";
        }
        else if(bst == BatteryManager.BATTERY_STATUS_FULL){
            status = "FULL";
        }
        else if(bst == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
            status = "NOT CHARGING";
        }
        else if(bst == BatteryManager.BATTERY_STATUS_UNKNOWN){
            status = "UNKNOWN";
        }

        return status;
    }

    private void batteryAgeing(){
        // RemainingCapacityRatio
        int remainingCapacityRatio = bManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CAPACITY);
        sbuilder.append("RemainingCapacityRatio: ");
        sbuilder.append(String.valueOf(remainingCapacityRatio));
        sbuilder.append(" %\n");

        // BatteryCapacityMicroAh
        int batteryCapacityMicroAh = bManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        sbuilder.append("BatteryCapacityMicroAh: ");
        sbuilder.append(String.valueOf(batteryCapacityMicroAh));
        sbuilder.append(" uAh\n");

        // DischargingTimeSeconds
        int dischargingTimeSeconds  = bManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
        sbuilder.append("DischargingTimeSeconds: ");
        sbuilder.append(String.valueOf(dischargingTimeSeconds));
        sbuilder.append(" msec\n");

        // Current
        int currentNow  = bManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        sbuilder.append("CurrentNow: ");
        sbuilder.append(String.valueOf(currentNow));
        sbuilder.append(" uA\n");

        // EnergyCounter
        long energyCounter  = bManager.getLongProperty(
                BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
        sbuilder.append("EnergyCounter: ");
        sbuilder.append(String.valueOf(energyCounter));
        sbuilder.append(" nWh\n\n");

    }
}
