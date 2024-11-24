package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class GameBundle extends Item{
    private Game[] games;
    public GameBundle(String title, BigDecimal price, LocalDateTime releaseDate, Game[] games)
    {
        super(title, price, releaseDate);
        this.games=games;
    }
}
