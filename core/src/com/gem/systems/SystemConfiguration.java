package com.gem.systems;

import com.gem.engine.EngineInfo;

import java.util.*;

public class SystemConfiguration {
    private Set<Class<?>> all;
    private List<BitSet> configuration = new ArrayList<BitSet>();
    private Set<Class<?>> or;

    private boolean unset = true;

    public SystemConfiguration() {
    }

    public SystemConfiguration(Set<Class<?>> All) {
        this.all = All;
    }

    public void addClass(Class<?>... cls) {
        all.addAll(Arrays.asList(cls));
    }

    public SystemConfiguration all(Class<?>... cls) {
        unset = false;
        all = new TreeSet<Class<?>>(new Comparator<Class<?>>() {

            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                if (o1.hashCode() > o2.hashCode())
                    return 1;

                if (o1.hashCode() < o2.hashCode())
                    return -1;
                return 0;
            }
        });
        all.addAll(Arrays.asList(cls));
        configure();
        return this;
    }

    public boolean equals(Set<Class<?>> obj) {
        return all.equals(obj);
    }

    public boolean equals(SystemConfiguration obj) {
        return all.equals(obj.getConfiguration());
    }

    public final List<BitSet> getConfiguration() {
        if (unset) {
            return null;
        }
        return configuration;
    }

    public SystemConfiguration or(Class<?>... cls) {
        unset = false;
        or = new TreeSet<Class<?>>(new Comparator<Class<?>>() {

            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                if (o1.hashCode() > o2.hashCode())
                    return 1;

                if (o1.hashCode() < o2.hashCode())
                    return -1;
                return 0;
            }
        });
        or.addAll(Arrays.asList(cls));
        configure();
        return this;
    }

    public void removeClass(Class<?>... cls) {
        all.removeAll(Arrays.asList(cls));
    }

    private void configure() {
        configuration.clear();
        if (or == null) {
            configuration.add(new BitSet((EngineInfo.TotalComponents)));
            for (Class<?> cls : all) {
                configuration.get(0).set(EngineInfo.ComponentIndexMap.get(cls));
            }
        } else {
            int i = 0;
            for (Class<?> cls : or) {
                configuration.add(new BitSet((EngineInfo.TotalComponents)));
                for (Class<?> cls2 : all) {
                    configuration.get(i).set(EngineInfo.ComponentIndexMap.get(cls2));
                }
                configuration.get(i).set(EngineInfo.ComponentIndexMap.get(cls));
                i++;
            }
        }
    }
}
