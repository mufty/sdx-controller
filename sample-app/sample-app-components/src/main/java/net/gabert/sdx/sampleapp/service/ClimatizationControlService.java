package net.gabert.sdx.sampleapp.service;

import net.gabert.sdx.heiko.component.Callback;
import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.component.ValueListener;
import net.gabert.sdx.heiko.ctx.Context;
import net.gabert.util.TimeStat;

import java.util.Map;

import static net.gabert.util.LogUtil.sout;

/**
 *
 * @author Robert Gallas
 */
public class ClimatizationControlService extends Service {
    @Override
    public void start(Map<String, Object> initParams) {
        Context thermometerLA = getPathContext("/las-vegas/big-house/thermometer");
        Context thermometerTO = getPathContext("/toronto/apartment/thermometer");

        Logic logicLA = new Logic("/las-vegas/big-house/clima");
        Logic logicTO = new Logic("/toronto/apartment/clima");

        thermometerLA.registerListener("/temperature", logicLA);
        thermometerTO.registerListener("/temperature", logicTO);
    }

    @Override
    public void stop() {}

    private class Logic implements ValueListener {

        private final Context clima;

        private Logic(String climaPath) {
            this.clima = getPathContext(climaPath);
        }

        @Override
        public void onDataChanged(Object value) {
            int temperature = ((Double)value).intValue();

            if (temperature < 20) {
                clima.setValue("/power", "OFF");
            } else if (temperature >= 20 && temperature < 30) {
                clima.setValue("/power", "MEDIUM");
            } else if (temperature >= 30) {
                clima.setValue("/power", "HIGH");
            }
        }
    }
}
