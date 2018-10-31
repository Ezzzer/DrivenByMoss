// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.kontrol.usb.mki.mode;

/**
 * Interface for specific knob and button interactions for modes.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface IKontrol1Mode
{
    /**
     * The main knob was turned.
     *
     * @param value The updated value
     */
    void onMainKnob (final int value);


    /**
     * The main knob was pressed.
     */
    void onMainKnobPressed ();


    /**
     * Scroll left.
     */
    void scrollLeft ();


    /**
     * Scroll right.
     */
    void scrollRight ();


    /**
     * Scroll up.
     */
    void scrollUp ();


    /**
     * Scroll down.
     */
    void scrollDown ();


    /**
     * The back button was pressed.
     */
    void onBack ();


    /**
     * The enter button was pressed.
     */
    void onEnter ();
}