package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SocialFeedPost implements Post {

    private final UUID uniqueId;
    private final UserProfile author;
    private String content;
    private final LocalDateTime publishedOn;
    private Map<ReactionType, Set<UserProfile>> reactions;

    public SocialFeedPost(UserProfile author, String content) {
        if (Objects.isNull(author)) {
            throw new IllegalArgumentException("Invalid author.");
        }

        this.author = author;
        uniqueId = UUID.randomUUID();
        setContent(content);
        publishedOn = LocalDateTime.now();
        reactions = new HashMap<>();
    }

    private void setContent(String content) {
        if (Objects.isNull(content) || content.isEmpty() || content.isBlank()) {
            throw new IllegalArgumentException("Invalid content.");
        }

        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialFeedPost that = (SocialFeedPost) o;
        return Objects.equals(uniqueId, that.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueId);
    }

    @Override
    public String getUniqueId() {
        return uniqueId.toString();
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (Objects.isNull(userProfile)) {
            throw new IllegalArgumentException("Given userProfile is null.");
        }

        if (Objects.isNull(reactionType)) {
            throw new IllegalArgumentException("Given reactionType is null.");
        }

        boolean hasReacted = removeReaction(userProfile);

        if (!reactions.containsKey(reactionType)) {
            reactions.put(reactionType, new HashSet<>());
        }
        reactions.get(reactionType).add(userProfile);

        return !hasReacted;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (Objects.isNull(userProfile)) {
            throw new IllegalArgumentException("Given userProfile is null.");
        }

        Iterator<ReactionType> keyIterator = reactions.keySet().iterator();
        while (keyIterator.hasNext()) {
            ReactionType key = keyIterator.next();
            Set<UserProfile> usersReactedWithKey = reactions.get(key);

            if (usersReactedWithKey.remove(userProfile)) {
                if (usersReactedWithKey.isEmpty()) {
                    keyIterator.remove();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        return Collections.unmodifiableMap(reactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (Objects.isNull(reactionType)) {
            throw new IllegalArgumentException("Reaction type is null.");
        }

        if (reactions.containsKey(reactionType)) {
            return reactions.get(reactionType).size();
        } else {
            return 0;
        }
    }

    @Override
    public int totalReactionsCount() {
        int result = 0;
        Set<ReactionType> keys = reactions.keySet();
        for (ReactionType reaction : keys) {
            result += reactions.get(reaction).size();
        }
        return result;
    }
}
