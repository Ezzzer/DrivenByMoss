// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2021
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.launchpad.view;

import de.mossgrabers.controller.launchpad.LaunchpadConfiguration;
import de.mossgrabers.controller.launchpad.controller.LaunchpadColorManager;
import de.mossgrabers.controller.launchpad.controller.LaunchpadControlSurface;
import de.mossgrabers.framework.configuration.AbstractConfiguration;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.grid.IPadGrid;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.ITransport;
import de.mossgrabers.framework.daw.constants.RecordQuantization;
import de.mossgrabers.framework.daw.constants.Resolution;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.INoteRepeat;
import de.mossgrabers.framework.featuregroup.AbstractView;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Simulates the missing buttons (in contrast to Launchpad Pro) on the grid.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class ShiftView extends AbstractView<LaunchpadControlSurface, LaunchpadConfiguration>
{
    private static final String TAG_ACTIVE = "Active";


    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model The model
     */
    public ShiftView (final LaunchpadControlSurface surface, final IModel model)
    {
        super ("Shift", surface, model);
    }

    final int CLIP_REC_AUTOMATION = 99;
    final int STOP = 91;
    final int PLAY = 83;
    final int RECORD = 75;
    final int ARR_OVERDUB = 67;

    final int NEW = 98;
    final int DOUBLE = 90;
    final int DUPLICATE = 82;
    final int ARR_REC_AUTOMATION = 74;

    final int DELETE = 97;
    final int UNDO = 89;
    final int REDO = 81;

    final int TAP_TEMPO = 96;
    final int METRONOME = 88;
    final int TOGGLE_ACCENT = 80;

    final int QUANTIZE = 95;
    final int REC_QUANTIZE = 87;
    final int ARR_LOOP = 79;

    final int ADD_TRACK_INST = 94;
    final int ADD_TRACK_AUDIO = 86;
    final int NOTE_REPEAT = 78;

    final int ADD_TRACK_EFFECT = 93;

    final int NEW_CLIP_LENGTH = 36;
   // final int REC_ARM = 45;

    /* the grid
     * 92 - 99
     * 84 - 91
     * 76 - 83
     * 68 - 75
     * 60 - 67
     * 52 - 59
     * 44 - 51
     * 36 - 43
     * */


    /**
     * {@inheritDoc}
     */
    @Override
    public void drawGrid ()
    {
        final IPadGrid padGrid = this.surface.getPadGrid ();
        final LaunchpadConfiguration configuration = this.surface.getConfiguration ();

        final ITransport transport = this.model.getTransport ();

        for (int i = 36; i < 100; i++)
            padGrid.light(i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);

        // Add tracks
        padGrid.light(ADD_TRACK_INST, LaunchpadColorManager.LAUNCHPAD_COLOR_GREEN);
        padGrid.light(ADD_TRACK_AUDIO, LaunchpadColorManager.LAUNCHPAD_COLOR_GREEN_SPRING);
        padGrid.light(ADD_TRACK_EFFECT, LaunchpadColorManager.LAUNCHPAD_COLOR_TURQUOISE_CYAN);

        // Accent on/off
        padGrid.light(TOGGLE_ACCENT, configuration.isAccentActive() ? LaunchpadColorManager.LAUNCHPAD_COLOR_YELLOW_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_YELLOW_LO);

        // New clip length
        final int clipLengthIndex = this.surface.getConfiguration ().getNewClipLength ();
        for (int i = 0; i < 8; i++)
            padGrid.light(NEW_CLIP_LENGTH + i, i == clipLengthIndex ? LaunchpadColorManager.LAUNCHPAD_COLOR_WHITE : LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_LO);

        // Note Repeat
        final INoteRepeat noteRepeat = this.surface.getMidiInput ().getDefaultNoteInput ().getNoteRepeat ();
        padGrid.light(NOTE_REPEAT, noteRepeat.isActive() ? LaunchpadColorManager.LAUNCHPAD_COLOR_ORCHID_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_ORCHID_LO);

        // Note Repeat period
        final int periodIndex = Resolution.getMatch (noteRepeat.getPeriod ());
        padGrid.light(NOTE_REPEAT - 8, periodIndex == 0 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);
        padGrid.light(NOTE_REPEAT - 16, periodIndex == 2 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);
        padGrid.light(NOTE_REPEAT - 24, periodIndex == 4 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);
        padGrid.light(NOTE_REPEAT - 32, periodIndex == 6 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);

        padGrid.light(NOTE_REPEAT - 7, periodIndex == 1 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);
        padGrid.light(NOTE_REPEAT - 15, periodIndex == 3 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);
        padGrid.light(NOTE_REPEAT - 23, periodIndex == 5 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);
        padGrid.light(NOTE_REPEAT - 31, periodIndex == 7 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);

        // Note Repeat length
        final int lengthIndex = Resolution.getMatch (noteRepeat.getNoteLength ());
        padGrid.light(NOTE_REPEAT - 6, lengthIndex == 0 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);
        padGrid.light(NOTE_REPEAT - 14, lengthIndex == 2 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);
        padGrid.light(NOTE_REPEAT - 22, lengthIndex == 4 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);
        padGrid.light(NOTE_REPEAT - 30, lengthIndex == 6 ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);

        padGrid.light(NOTE_REPEAT - 5, lengthIndex == 1 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);
        padGrid.light(NOTE_REPEAT - 13, lengthIndex == 3 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);
        padGrid.light(NOTE_REPEAT - 21, lengthIndex == 5 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);
        padGrid.light(NOTE_REPEAT - 29, lengthIndex == 7 ? LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_PINK_LO);

        // Stop all
        padGrid.light(STOP, LaunchpadColorManager.LAUNCHPAD_COLOR_RED);

        if (this.surface.isPro ())
        {
            for (int i = 44; i < 51; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            for (int i = 52; i < 55; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            for (int i = 59; i < 63; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            for (int i = 67; i < 71; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            for (int i = 75; i < 79; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            for (int i = 83; i < 87; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            for (int i = 88; i < 91; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            for (int i = 92; i < 97; i++)
                padGrid.light (i, LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK);
            return;
        }

        // Record
        padGrid.light(RECORD, transport.isRecording() ? LaunchpadColorManager.LAUNCHPAD_COLOR_RED_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_RED_LO);
        padGrid.light(CLIP_REC_AUTOMATION, transport.isWritingClipLauncherAutomation() ? LaunchpadColorManager.LAUNCHPAD_COLOR_AMBER_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_AMBER_LO);
        padGrid.light(ARR_REC_AUTOMATION, transport.isWritingArrangerAutomation() ? LaunchpadColorManager.LAUNCHPAD_COLOR_AMBER_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_AMBER_LO);
        padGrid.light(ARR_OVERDUB, transport.isArrangerOverdub() ? LaunchpadColorManager.LAUNCHPAD_COLOR_ROSE : LaunchpadColorManager.LAUNCHPAD_COLOR_RED_LO);

        // Play / New
        padGrid.light(PLAY, transport.isPlaying() ? LaunchpadColorManager.LAUNCHPAD_COLOR_GREEN_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_GREEN_LO);
        padGrid.light(NEW, LaunchpadColorManager.LAUNCHPAD_COLOR_GREEN_SPRING);

        // Duplicate
        if (configuration.isDuplicateModeActive()) {
            padGrid.light(DUPLICATE, LaunchpadColorManager.LAUNCHPAD_COLOR_BLUE, LaunchpadColorManager.LAUNCHPAD_COLOR_OCEAN_BLUE, true);
        } else {
            padGrid.light(DUPLICATE, LaunchpadColorManager.LAUNCHPAD_COLOR_BLUE);
        }
        // Double
        padGrid.light(DOUBLE, LaunchpadColorManager.LAUNCHPAD_COLOR_BLUE_ORCHID);

        // Quantize
        padGrid.light(QUANTIZE, LaunchpadColorManager.LAUNCHPAD_COLOR_LIME_GREEN);
        // Record Quantization
        padGrid.light(REC_QUANTIZE, getQuantizeColor());

        // Delete
        if (configuration.isDeleteModeActive()) {
            padGrid.light(DELETE, LaunchpadColorManager.LAUNCHPAD_COLOR_MAGENTA, LaunchpadColorManager.LAUNCHPAD_COLOR_MAGENTA_PINK, true);
        } else {
            padGrid.light(DELETE, LaunchpadColorManager.LAUNCHPAD_COLOR_MAGENTA);
        }
        padGrid.light(ARR_LOOP, transport.isLoop() ? LaunchpadColorManager.LAUNCHPAD_COLOR_LIME_GREEN : LaunchpadColorManager.LAUNCHPAD_COLOR_GREEN_HI);


        // Undo / Redo
        padGrid.light(UNDO, LaunchpadColorManager.LAUNCHPAD_COLOR_AMBER);
        padGrid.light(REDO, LaunchpadColorManager.LAUNCHPAD_COLOR_AMBER_YELLOW);

        // Metronome
        padGrid.light(METRONOME, transport.isMetronomeOn() ? LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_HI : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY_LO);
        // Tap Tempo
        padGrid.light(TAP_TEMPO, LaunchpadColorManager.LAUNCHPAD_COLOR_GREY_HALF);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onGridNote (final int note, final int velocity)
    {
        if (velocity == 0)
            return;

        final LaunchpadConfiguration configuration = this.surface.getConfiguration ();

        switch (note)
        {
            case NEW_CLIP_LENGTH:
            case NEW_CLIP_LENGTH + 1:
            case NEW_CLIP_LENGTH + 2:
            case NEW_CLIP_LENGTH + 3:
            case NEW_CLIP_LENGTH + 4:
            case NEW_CLIP_LENGTH + 5:
            case NEW_CLIP_LENGTH + 6:
            case NEW_CLIP_LENGTH + 7:
                final int newClipLength = note - NEW_CLIP_LENGTH;
                configuration.setNewClipLength (newClipLength);
                this.surface.getDisplay ().notify ("New clip length: " + AbstractConfiguration.getNewClipLengthValue (newClipLength));
                return;

            case STOP:
                this.model.getCurrentTrackBank ().stop ();
                this.surface.getDisplay().notify("Clip Stop");
                return;

            case NOTE_REPEAT:
                configuration.toggleNoteRepeatActive ();
                this.mvHelper.delayDisplay ( () -> "Note Repeat: " + (configuration.isNoteRepeatActive () ? TAG_ACTIVE : "Off"));
                return;

            case NOTE_REPEAT - 8:
                this.setPeriod (0);
                return;
            case NOTE_REPEAT - 7:
                this.setPeriod (1);
                return;
            case NOTE_REPEAT - 16:
                this.setPeriod (2);
                return;
            case NOTE_REPEAT - 15:
                this.setPeriod (3);
                return;
            case NOTE_REPEAT - 24:
                this.setPeriod (4);
                return;
            case NOTE_REPEAT - 23:
                this.setPeriod (5);
                return;
            case NOTE_REPEAT - 32:
                this.setPeriod (6);
                return;
            case NOTE_REPEAT - 31:
                this.setPeriod (7);
                return;

            case NOTE_REPEAT - 6:
                this.setNoteLength (0);
                return;
            case NOTE_REPEAT - 5:
                this.setNoteLength (1);
                return;
            case NOTE_REPEAT - 14:
                this.setNoteLength (2);
                return;
            case NOTE_REPEAT - 13:
                this.setNoteLength (3);
                return;
            case NOTE_REPEAT - 22:
                this.setNoteLength (4);
                return;
            case NOTE_REPEAT - 21:
                this.setNoteLength (5);
                return;
            case NOTE_REPEAT - 30:
                this.setNoteLength (6);
                return;
            case NOTE_REPEAT - 29:
                this.setNoteLength (7);
                return;

            case TOGGLE_ACCENT:
                final boolean enabled = !configuration.isAccentActive ();
                configuration.setAccentEnabled (enabled);
                this.surface.getDisplay ().notify ("Fixed Accent: " + (enabled ? "On" : "Off"));
                return;

            case ADD_TRACK_INST:
                this.model.getApplication().addInstrumentTrack();
                return;
            case ADD_TRACK_AUDIO:
                this.model.getApplication().addAudioTrack();
                return;
            case ADD_TRACK_EFFECT:
                this.model.getApplication ().addEffectTrack ();
                return;
            default:
                // Fall through to be handled below
                break;
        }

        if (this.surface.isPro ())
            return;

        switch (note)
        {
            case METRONOME:
                this.executeNormal(ButtonID.METRONOME, ButtonEvent.UP);
                this.mvHelper.delayDisplay ( () -> "Metronome: " + (this.model.getTransport ().isMetronomeOn () ? "On" : "Off"));
                break;
            case TAP_TEMPO:
                this.executeShifted(ButtonID.METRONOME, ButtonEvent.UP);
                this.surface.getDisplay ().notify ("Tap Tempo");
                break;
            case UNDO:
                this.executeNormal(ButtonID.UNDO, ButtonEvent.UP);
                this.surface.getDisplay ().notify ("Undo");
                break;
            case REDO:
                this.executeShifted(ButtonID.UNDO, ButtonEvent.UP);
                this.surface.getDisplay ().notify ("Redo");
                break;
            case DELETE:
                configuration.toggleDeleteModeActive ();
                this.surface.getDisplay ().notify ("Delete " + (configuration.isDeleteModeActive () ? TAG_ACTIVE : "Off"));
                break;
            case ARR_LOOP:
                this.executeShifted(ButtonID.DELETE, ButtonEvent.DOWN);
                this.mvHelper.delayDisplay ( () -> "Arrangement Loop: " + (this.model.getTransport ().isLoop () ? "On" : "Off"));
                break;
            case REC_QUANTIZE:
                this.executeNormal(ButtonID.REC_QUANTIZE, ButtonEvent.DOWN);
                break;
            case QUANTIZE:
                this.executeNormal(ButtonID.QUANTIZE, ButtonEvent.DOWN);
                this.surface.getDisplay ().notify ("Quantize");
                break;
            case DUPLICATE:
                configuration.toggleDuplicateModeActive ();
                this.surface.getDisplay ().notify ("Duplicate " + (configuration.isDuplicateModeActive () ? TAG_ACTIVE : "Off"));
                break;
            case DOUBLE:
                this.executeShifted(ButtonID.DUPLICATE, ButtonEvent.DOWN);
                this.surface.getDisplay ().notify ("Double");
                break;
            case PLAY:
                this.executeNormal(ButtonID.PLAY, ButtonEvent.DOWN);
                this.surface.getDisplay ().notify ("Play");
                break;
            case NEW:
                this.executeShifted(ButtonID.NEW, ButtonEvent.DOWN);
                this.surface.getDisplay ().notify ("New");
                break;
            case RECORD:
                this.executeNormal(ButtonID.RECORD, ButtonEvent.UP);
                this.surface.getDisplay ().notify ("Arranger record");
                break;
            case ARR_OVERDUB:
                this.executeNormal(ButtonID.ARR_OVERDUB, ButtonEvent.UP);
                break;

            case CLIP_REC_AUTOMATION:
                this.executeNormal(ButtonID.CLIP_REC_AUTOMATION, ButtonEvent.UP);
                break;

            case ARR_REC_AUTOMATION:
                this.executeNormal(ButtonID.ARR_REC_AUTOMATION, ButtonEvent.UP);
                break;
            default:
                // Not used
                break;
        }
    }


    /** {@inheritDoc} */
    @Override
    public int getButtonColor (final ButtonID buttonID)
    {
        final boolean isPro = this.surface.isPro ();
        switch (buttonID)
        {
            case SCENE1:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_CYAN;
            case SCENE2:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_SKY;
            case SCENE3:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_ORCHID;
            case SCENE4:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_GREEN;
            case SCENE5:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_ROSE;
            case SCENE6:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_YELLOW;
            case SCENE7:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_BLUE;
            case SCENE8:
                return isPro ? LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK : LaunchpadColorManager.LAUNCHPAD_COLOR_RED;
            default:
                return LaunchpadColorManager.LAUNCHPAD_COLOR_BLACK;
        }
    }


    private void handleControlModes (final ButtonID commandID)
    {
        this.surface.getButton (commandID).getCommand ().execute (ButtonEvent.DOWN, 127);
    }


    /** {@inheritDoc} */
    @Override
    public void onButton (final ButtonID buttonID, final ButtonEvent event, final int velocity)
    {
        if (this.surface.isPro () || event != ButtonEvent.DOWN)
            return;

        switch (buttonID)
        {
            case SCENE1:
                this.handleControlModes (ButtonID.VOLUME);
                break;
            case SCENE2:
                this.handleControlModes (ButtonID.PAN_SEND);
                break;
            case SCENE3:
                this.handleControlModes (ButtonID.SENDS);
                break;
            case SCENE4:
                this.handleControlModes (ButtonID.TRACK);
                break;
            case SCENE5:
                this.handleControlModes (ButtonID.STOP_CLIP);
                break;
            case SCENE6:
                this.handleControlModes (ButtonID.MUTE);
                break;
            case SCENE7:
                this.handleControlModes (ButtonID.SOLO);
                break;
            case SCENE8:
                this.handleControlModes (ButtonID.REC_ARM);
                break;
            default:
                // Not used
                break;
        }
    }


    private void setPeriod (final int index)
    {
        this.surface.getConfiguration ().setNoteRepeatPeriod (Resolution.values ()[index]);
        this.surface.scheduleTask ( () -> this.surface.getDisplay ().notify ("Period: " + Resolution.getNameAt (index)), 100);
    }


    private void setNoteLength (final int index)
    {
        this.surface.getConfiguration ().setNoteRepeatLength (Resolution.values ()[index]);
        this.surface.scheduleTask ( () -> this.surface.getDisplay ().notify ("Note Length: " + Resolution.getNameAt (index)), 100);
    }


    private void executeNormal(final ButtonID buttonID, final ButtonEvent event) {
        simulateButtonPress(buttonID);
    }


    private void executeShifted(final ButtonID buttonID, final ButtonEvent event) {
        simulateShiftedButtonPress(buttonID);
    }

    private int getQuantizeColor() {

        int defaultColor = LaunchpadColorManager.LAUNCHPAD_COLOR_TURQUOISE_HI;
        final ITrack cursorTrack = this.model.getCursorTrack();
        if (!cursorTrack.doesExist())
            return defaultColor - 5;

        final RecordQuantization[] values = RecordQuantization.values();
        final RecordQuantization recordQuantization = cursorTrack.getRecordQuantizationGrid();
        int index = 0;
        for (int i = 0; i < values.length; i++) {
            if (recordQuantization == values[i]) {
                index = i;
                break;
            }
        }
        return defaultColor - index;
    }


}