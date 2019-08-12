package com.jcs.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.jcs.consumer.filter.HttpTraceLogFilter;
import com.jcs.consumer.threadPool.ThreadPools;
import com.jcs.provider.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther xukh
 * @date 2019/8/2 19:37
 */
@Service
public class ConsumerServiceImpl implements ConsumerService{

    private static final Logger log = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    @Reference
    private ProviderService providerService;

    @Override
    public String get() {
        return "consumer";
    }

    @Override
    public String getProvider() {
        return providerService.get();
    }

    @Override
    public String executeThreadPool() {
        try {
            ThreadPools instance = ThreadPools.getInstance();
            instance.traceIdThreadPool(new Runnable() {
                @Override
                public void run() {
                    String traceId = RpcContext.getContext().getAttachment(HttpTraceLogFilter.TRACE_ID);
                    log.info("traceId={}",traceId);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return "finish";
    }


}
