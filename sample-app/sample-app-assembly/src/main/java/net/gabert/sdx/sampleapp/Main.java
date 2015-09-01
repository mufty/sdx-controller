package net.gabert.sdx.sampleapp;

import net.gabert.sdx.heiko.core.Controller;

/**
 *
 * @author Robert Gallas
 */
public class Main {
    public static void main(String[] args) {
        Controller controller = Controller.boot();
        controller.shutDown();
    }
}
