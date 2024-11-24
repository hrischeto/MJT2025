package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Comparator;

public class UserProfileByNumberOfFriends implements Comparator<UserProfile> {

    @Override
    public int compare(UserProfile first, UserProfile second) {
        if (first.equals(second)) {
            return 0;
        }
        return Integer.compare(first.getFriends().size(), second.getFriends().size());
    }
}
