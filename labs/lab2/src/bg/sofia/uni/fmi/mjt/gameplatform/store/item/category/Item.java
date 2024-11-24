package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

abstract public class Item implements StoreItem{

    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private double accumulatedRating;
    private int timesRated;

    public Item(String title, BigDecimal price, LocalDateTime releaseDate){
        setTitle(title);
        setPrice(price);
        setReleaseDate(releaseDate);
    }

    @Override
    public String getTitle()
    {
        return title;
    }
    @Override
    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.UP);
    }

    @Override
    public double getRating() {
        return accumulatedRating/timesRated;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating)
    {
            accumulatedRating += rating;
            timesRated++;
    }
}
