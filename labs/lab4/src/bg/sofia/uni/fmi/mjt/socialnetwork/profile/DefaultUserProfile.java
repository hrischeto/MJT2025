package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {

    private final String username;
    private Set<Interest> interests;
    private Set<UserProfile> friends;

    public DefaultUserProfile(String username) {
        this.username = username;
        interests = EnumSet.noneOf(Interest.class);
        friends = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUserProfile that = (DefaultUserProfile) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (Objects.isNull(interest)) {
            throw new IllegalArgumentException("Interest to be added is null");
        }

        if (interests.contains(interest)) {
            return false;
        } else {
            interests.add(interest);
            return true;
        }
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (Objects.isNull(interest)) {
            throw new IllegalArgumentException("Interest to be removed is null");
        }

        if (interests.isEmpty()) {
            return false;
        }

        if (interests.contains(interest)) {
            interests.remove(interest);
            return true;
        }

        return false;
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (Objects.isNull(userProfile)) {
            throw new IllegalArgumentException("Friend to be added is null");
        }
        if (userProfile.equals(this)) {
            throw new IllegalArgumentException("Cannot befriend yourself.");
        }

        if (friends.contains(userProfile)) {
            return false;
        } else {
            friends.add(userProfile);
            userProfile.addFriend((this));
            return true;
        }
    }

    public boolean unfriend(UserProfile userProfile) {
        if (Objects.isNull(userProfile)) {
            throw new IllegalArgumentException("Friend to be removed is null");
        }

        if (friends.contains(userProfile)) {
            friends.remove(userProfile);
            userProfile.unfriend(this);
            return true;
        } else {
            return false;
        }
    }

    public boolean isFriend(UserProfile userProfile) {
        if (Objects.isNull(userProfile)) {
            throw new IllegalArgumentException("Given friend is null");
        }

        return friends.contains(userProfile);
    }

}
