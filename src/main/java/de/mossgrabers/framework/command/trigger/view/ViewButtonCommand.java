// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.command.trigger.view;

import de.mossgrabers.framework.command.core.AbstractTriggerCommand;
import de.mossgrabers.framework.configuration.Configuration;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.IControlSurface;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.featuregroup.View;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Command to relay a button event to the active view.
 *
 * @param <S> The type of the control surface
 * @param <C> The type of the configuration
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class ViewButtonCommand<S extends IControlSurface<C>, C extends Configuration> extends AbstractTriggerCommand<S, C>
{
    protected ButtonID buttonID;


    /**
     * Constructor.
     *
     * @param buttonID The button which events to relay
     * @param surface The surface
     */
    public ViewButtonCommand (final ButtonID buttonID, final S surface)
    {
        this (buttonID, null, surface);
    }


    /**
     * Constructor.
     *
     * @param buttonID The button which events to relay
     * @param model The model
     * @param surface The surface
     */
    public ViewButtonCommand (final ButtonID buttonID, final IModel model, final S surface)
    {
        super (model, surface);

        this.buttonID = buttonID;
    }


    /** {@inheritDoc} */
    @Override
    public void execute (final ButtonEvent event, final int velocity)
    {
        final View view = this.surface.getViewManager ().getActive ();
        if (view != null)
            view.onButton (this.buttonID, event, velocity);
    }
}
