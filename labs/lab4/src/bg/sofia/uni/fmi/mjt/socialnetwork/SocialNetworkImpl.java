package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfileByNumberOfFriends;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Collections;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {

    private Set<UserProfile> users;
    private Set<Post> posts;

    public SocialNetworkImpl() {
        users = new HashSet<>();
        posts = new HashSet<>();
    }

    private boolean search(UserProfile searchedUser, UserProfile root) {
        Queue<UserProfile> queue = new ArrayDeque<>();
        queue.add(root);

        Set<UserProfile> visited = new HashSet<>();
        visited.add(root);

        while (!queue.isEmpty()) {
            UserProfile currentNode = queue.poll();

            if (currentNode.equals(searchedUser)) {
                return true;
            }

            for (UserProfile friend : currentNode.getFriends()) {
                // Only add friends who haven't been visited yet
                if (!visited.contains(friend)) {
                    queue.add(friend);
                    visited.add(friend);
                }
            }

        }
        return false;
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (Objects.isNull(userProfile)) {
            throw new IllegalArgumentException("User to be registered is null.");
        }
        if (users.contains(userProfile)) {
            throw new UserRegistrationException("User already registered.");
        }

        users.add(userProfile);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (Objects.isNull(userProfile)) {
            throw new IllegalArgumentException("User to be registered is null.");
        }
        if (Objects.isNull(content)) {
            throw new IllegalArgumentException("Given content is null.");
        }
        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("No such user registered.");
        }

        Post toAdd = new SocialFeedPost(userProfile, content);
        posts.add(toAdd);
        return toAdd;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableSet(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (Objects.isNull(post)) {
            throw new IllegalArgumentException("Post is null.");
        }

        Set<UserProfile> reached = new HashSet<>();
        UserProfile postAuthor = post.getAuthor();
        for (UserProfile user : users) {
            if (user.equals(postAuthor)) {
                continue;
            }
            if ((!postAuthor.getInterests().isEmpty() && !user.getInterests().isEmpty()) &&
                !Collections.disjoint(postAuthor.getInterests(), user.getInterests()) &&
                search(user, postAuthor)) {
                reached.add(user);
            }
        }

        return reached;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
        throws UserRegistrationException {
        if (Objects.isNull(userProfile1)) {
            throw new IllegalArgumentException("UserProfile1 is null.");
        }
        if (Objects.isNull(userProfile2)) {
            throw new IllegalArgumentException("UserProfile2 is null.");
        }

        if (!users.contains(userProfile1)) {
            throw new UserRegistrationException("User1 not registered.");
        }
        if (!users.contains(userProfile2)) {
            throw new UserRegistrationException("User2 not registered.");
        }

        Set<UserProfile> mutual = new HashSet<>(userProfile1.getFriends());
        mutual.retainAll(userProfile2.getFriends());

        return mutual;
    }

    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sorted = new TreeSet<>(new UserProfileByNumberOfFriends());
        sorted.addAll(users);
        return sorted;
    }
}
