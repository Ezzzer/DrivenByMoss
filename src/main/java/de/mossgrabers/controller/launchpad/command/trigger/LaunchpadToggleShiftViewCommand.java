// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2021
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.launchpad.command.trigger;

import de.mossgrabers.controller.launchpad.LaunchpadConfiguration;
import de.mossgrabers.controller.launchpad.controller.LaunchpadControlSurface;
import de.mossgrabers.framework.command.trigger.view.ToggleShiftViewCommand;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.featuregroup.ViewManager;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.Views;


/**
 * Command to show/hide the shift view. Additionally, returns to previous .
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class LaunchpadToggleShiftViewCommand extends ToggleShiftViewCommand<LaunchpadControlSurface, LaunchpadConfiguration> {
    /**
     * Constructor.
     *
     * @param model   The model
     * @param surface The surface
     */
    public LaunchpadToggleShiftViewCommand(final IModel model, final LaunchpadControlSurface surface) {
        super(model, surface);
    }

    /**
     * {@inheritDoc}
     */
    static long lastClick;
    final int shiftDelayTime = 100; // the time we give for rec_arm activation, otherwise the shift view will display

    @Override
    public void execute(final ButtonEvent event, final int velocity) {

        final ViewManager viewManager = this.surface.getViewManager();
        if(fastClick(event, viewManager)){
            return;
        }

        if (event == ButtonEvent.UP && viewManager.isActive(Views.SHIFT)) {
            if (surface.getConfiguration().isDuplicateModeActive() || surface.getConfiguration().isDeleteModeActive()) {
                viewManager.setActive(Views.SESSION);// ezer activate session view, there is nothing todo in other views
            }else {
                viewManager.restore();
            }
        }
        super.execute(event,velocity);
    }

    protected boolean fastClick(ButtonEvent event, ViewManager viewManager) {
        if (event == ButtonEvent.DOWN && !viewManager.isActive(Views.SHIFT)) {
            lastClick = System.currentTimeMillis();
            surface.scheduleTask(() -> {
                if (restoreFromTempoAndShuffle(event, viewManager)) return;
                exitOrEnterShiftView(event, viewManager);
            }, shiftDelayTime);
            return true;
        } else if (event == ButtonEvent.UP && (!surface.isLongPressed(ButtonID.SHIFT))) {
            if (System.currentTimeMillis() - lastClick < shiftDelayTime) {
                this.model.getTransport().toggleLauncherOverdub();
                this.mvHelper.delayDisplay(() -> "Clip Overdub: " + (this.model.getTransport().isLauncherOverdub() ? "On" : "Off"));
                return true;
            }
        }
        return false;
    }

    private boolean restoreFromTempoAndShuffle(ButtonEvent event, ViewManager viewManager) {
        if (event == ButtonEvent.LONG) {
            return true;
        }

        if (event == ButtonEvent.DOWN && (viewManager.isActive(Views.TEMPO) || viewManager.isActive(Views.SHUFFLE))) {
            viewManager.restore();
            this.surface.setTriggerConsumed(ButtonID.SHIFT);
            return true;
        }
        return false;
    }
}
