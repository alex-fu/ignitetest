package com.github.fyfhust;

import com.github.fyfhust.model.Student;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.TransactionConfiguration;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;

public class IgniteSingleton {
    static Ignite ignite;
    static IgniteCache<String, Student> studentCache;

    static {
        IgniteConfiguration igniteCfg = new IgniteConfiguration();
        igniteCfg.setClientMode(true);

        TransactionConfiguration transactionConfiguration = new TransactionConfiguration();
        transactionConfiguration.setDefaultTxConcurrency(TransactionConcurrency.OPTIMISTIC);
        transactionConfiguration.setDefaultTxIsolation(TransactionIsolation.SERIALIZABLE);
        igniteCfg.setTransactionConfiguration(transactionConfiguration);

        ignite = Ignition.start(igniteCfg);

        CacheConfiguration<String, Student> ccfg = new CacheConfiguration<String, Student>("student");
        ccfg.setBackups(1);
        ccfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

        ccfg.setIndexedTypes(String.class, Student.class);

        studentCache = ignite.getOrCreateCache(ccfg);
    }

}
