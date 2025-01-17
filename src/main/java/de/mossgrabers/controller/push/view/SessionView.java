// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2021
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.push.view;

import de.mossgrabers.controller.push.PushConfiguration;
import de.mossgrabers.controller.push.command.trigger.SelectSessionViewCommand;
import de.mossgrabers.controller.push.controller.PushColorManager;
import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.IScene;
import de.mossgrabers.framework.daw.data.bank.ISceneBank;
import de.mossgrabers.framework.featuregroup.AbstractFeatureGroup;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.AbstractSessionView;
import de.mossgrabers.framework.view.SessionColor;
import de.mossgrabers.framework.view.TransposeView;


/**
 * The Session view.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class SessionView extends AbstractSessionView<PushControlSurface, PushConfiguration> implements TransposeView
{
    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model The model
     */
    public SessionView (final PushControlSurface surface, final IModel model)
    {
        super ("Session", surface, model, 8, 8, true);

        final boolean isPush2 = this.surface.getConfiguration ().isPush2 ();
        final int redLo = isPush2 ? PushColorManager.PUSH2_COLOR2_RED_LO : PushColorManager.PUSH1_COLOR2_RED_LO;
        final int redHi = isPush2 ? PushColorManager.PUSH2_COLOR2_RED_HI : PushColorManager.PUSH1_COLOR2_RED_HI;
        final int black = isPush2 ? PushColorManager.PUSH2_COLOR2_BLACK : PushColorManager.PUSH1_COLOR2_BLACK;
        final int white = isPush2 ? PushColorManager.PUSH2_COLOR2_WHITE : PushColorManager.PUSH1_COLOR2_WHITE;
        final int green = isPush2 ? PushColorManager.PUSH2_COLOR2_GREEN : PushColorManager.PUSH1_COLOR2_GREEN;
        final int amber = isPush2 ? PushColorManager.PUSH2_COLOR2_AMBER : PushColorManager.PUSH1_COLOR2_AMBER;
        final SessionColor isRecording = new SessionColor (redHi, redHi, false);
        final SessionColor isRecordingQueued = new SessionColor (redHi, black, true);
        final SessionColor isPlaying = new SessionColor (green, green, false);
        final SessionColor isPlayingQueued = new SessionColor (green, green, true);
        final SessionColor hasContent = new SessionColor (amber, white, false);
        final SessionColor noContent = new SessionColor (black, -1, false);
        final SessionColor recArmed = new SessionColor (redLo, -1, false);
        this.setColors (isRecording, isRecordingQueued, isPlaying, isPlayingQueued, hasContent, noContent, recArmed);

        this.birdColorHasContent = hasContent;
        this.birdColorSelected = isPlaying;
    }


    /** {@inheritDoc} */
    @Override
    public void onGridNote (final int note, final int velocity)
    {
        if (velocity == 0)
            ((SelectSessionViewCommand) this.surface.getButton (ButtonID.SESSION).getCommand ()).setTemporary ();

        // Birds-eye-view navigation
        if (this.isBirdsEyeActive ())
        {
            if (velocity == 0)
                return;

            final int index = note - 36;
            final int x = index % this.columns;
            final int y = this.rows - 1 - index / this.columns;

            this.onGridNoteBirdsEyeView (x, y, 0);
            return;
        }

        super.onGridNote (note, velocity);
    }


    /** {@inheritDoc} */
    @Override
    public String getButtonColorID (final ButtonID buttonID)
    {
        final int scene = buttonID.ordinal () - ButtonID.SCENE1.ordinal ();
        if (scene < 0 || scene >= 8)
            return AbstractFeatureGroup.BUTTON_COLOR_OFF;

        final ISceneBank sceneBank = this.model.getSceneBank ();
        final IScene s = sceneBank.getItem (scene);
        if (s.doesExist ())
            return s.isSelected () ? AbstractSessionView.COLOR_SELECTED_SCENE : AbstractSessionView.COLOR_SCENE;
        return AbstractSessionView.COLOR_SCENE_OFF;
    }


    /** {@inheritDoc} */
    @Override
    public void onOctaveDown (final ButtonEvent event)
    {
        if (event == ButtonEvent.DOWN)
            this.model.getCurrentTrackBank ().getSceneBank ().selectNextPage ();
    }


    /** {@inheritDoc} */
    @Override
    public void onOctaveUp (final ButtonEvent event)
    {
        if (event == ButtonEvent.DOWN)
            this.model.getCurrentTrackBank ().getSceneBank ().selectPreviousPage ();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isOctaveUpButtonOn ()
    {
        return this.model.getCurrentTrackBank ().getSceneBank ().canScrollPageForwards ();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isOctaveDownButtonOn ()
    {
        return this.model.getCurrentTrackBank ().getSceneBank ().canScrollPageBackwards ();
    }


    /** {@inheritDoc} */
    @Override
    public void onButton (final ButtonID buttonID, final ButtonEvent event, final int velocity)
    {
        super.onButton (buttonID, event, velocity);

        if (ButtonID.isSceneButton (buttonID) && event == ButtonEvent.UP)
            ((SelectSessionViewCommand) this.surface.getButton (ButtonID.SESSION).getCommand ()).setTemporary ();
    }
}