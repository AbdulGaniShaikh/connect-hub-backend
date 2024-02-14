package com.shaikhabdulgani.ConnectHub.util;

import java.util.Random;

/**
 * Utility class for providing default user descriptions.
 * This class contains a collection of witty default descriptions.
 */
public class DefaultDescription {

    /**
     * Array of witty default descriptions.
     */
    private static final String[] descriptions = new String[]{
            "Oh, so you're the CEO of procrastination incorporated, yet somehow you can't find the time to update your profile?",
            "Wow, your profile screams 'world traveler' but your bio whispers 'I haven't left my couch in weeks'.",
            "Impressive, you've mastered the art of avoiding profile updates while simultaneously mastering Candy Crush.",
            "Bravo! You've successfully explored the depths of Netflix's catalog, yet the mystery of updating your profile remains unsolved.",
            "Your profile says 'living life to the fullest', but your bio suggests you're living it on standby.",
            "You've clearly unlocked the 'worldly adventurer' achievement, but updating your bio must be a secret boss level.",
            "Your profile screams 'dynamic individual', but your bio whispers 'dynamic at avoiding updates'.",
            "You've clearly won the award for 'Most Traveled in Self-Deception', but updating your bio seems like an unattainable quest.",
            "Your profile photo screams 'carpe diem', but your bio whispers 'carpe procrastination'.",
            "Ah, the wanderlust is strong in this one... except when it comes to updating their profile, of course."
    };

    /**
     * Gets a random default description.
     *
     * @return A randomly selected default description
     */
    public static String getRandomDescription() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(descriptions.length);
        return descriptions[randomIndex];
    }
}

