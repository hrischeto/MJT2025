package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;
import java.util.Arrays;
import java.math.BigDecimal;

public class GameStore implements StoreAPI{

    private StoreItem[] availableItems;
    boolean discounted=false;

    private void applyDiscount(double val)
    {
        BigDecimal promo=new BigDecimal(val);

        for(StoreItem item:availableItems)
        {
            item.setPrice(item.getPrice().subtract(item.getPrice().multiply(promo)));
        }

        discounted =true;
    }

    public GameStore(StoreItem[] availableItems)
        {
            this.availableItems=availableItems;
            discounted=false;
        }

    @Override
   public StoreItem[] findItemByFilters(ItemFilter[] itemFilters)
    {
        boolean[] filtered=new boolean[availableItems.length];
        Arrays.fill(filtered, true);
        int mismatched=0;

        for(int i=0;i<filtered.length;i++)
        {
            for (ItemFilter itemFilter : itemFilters) {
                if (!filtered[i] || !itemFilter.matches((availableItems[i]))) {
                    filtered[i] = false;
                    mismatched++;
                    break;
                }
            }
        }

        StoreItem[] toReturn =new StoreItem[availableItems.length-mismatched];

        for(int i=0;i<filtered.length;i++)
        {
            if(filtered[i])
            {
                toReturn[i]=availableItems[i];
            }
        }
        return toReturn;
    }

    @Override
    public void applyDiscount(String promoCode)
    {
        if(discounted)
            return;

        switch (promoCode) {
            case "100YO" -> applyDiscount(1);
            case "VAN40" -> applyDiscount(0.4);
        }

    }

    @Override
   public boolean rateItem(StoreItem item, int rating)
    {
        if(rating>=1&&rating<=5) {
            item.rate(rating);
            return true;
        }

        return false;
    }
    
}
