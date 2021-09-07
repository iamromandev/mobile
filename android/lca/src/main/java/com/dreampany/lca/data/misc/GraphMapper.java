package com.dreampany.lca.data.misc;

import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.util.TimeUtil;
import com.dreampany.lca.api.cmc.model.CmcGraph;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.misc.GraphAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class GraphMapper {

    private final SmartMap<String, Graph> map;
    private final SmartCache<String, Graph> cache;

    @Inject
    GraphMapper(@GraphAnnote SmartMap<String, Graph> map,
                @GraphAnnote SmartCache<String, Graph> cache) {
        this.map = map;
        this.cache = cache;
    }

    public Graph toGraph(CmcGraph in) {
        if (in == null) {
            return null;
        }

        String id = in.getSlug();
        Graph out = map.get(id);
        if (out == null) {
            out = new Graph();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setSlug(in.getSlug());
        out.setStartTime(in.getStartTime());
        out.setEndTime(in.getEndTime());
        out.setPriceBtc(in.getPriceBtc());
        out.setPriceUsd(in.getPriceUsd());
        out.setVolumeUsd(in.getVolumeUsd());
        return out;
    }

    public boolean hasGraph(String id) {
        return map.contains(id);
    }

    public Graph getGraph(String id) {
        return map.get(id);
    }

}
