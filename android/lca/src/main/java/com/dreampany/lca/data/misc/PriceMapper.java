package com.dreampany.lca.data.misc;

import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.lca.data.model.Price;
import com.dreampany.lca.misc.PriceAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/9/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class PriceMapper {

    private final SmartMap<String, Price> map;
    private final SmartCache<String, Price> cache;

    @Inject
    PriceMapper(@PriceAnnote SmartMap<String, Price> map,
                @PriceAnnote SmartCache<String, Price> cache) {
        this.map = map;
        this.cache = cache;
    }
}
