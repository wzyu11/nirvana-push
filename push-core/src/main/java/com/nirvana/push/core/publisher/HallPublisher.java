package com.nirvana.push.core.publisher;

import com.nirvana.push.core.broker.MessageHall;

import java.util.Collection;

/**
 * 发布到MessageHall。
 * Created by Nirvana on 2017/9/3.
 */
public interface HallPublisher extends Publisher {

    void publish(MessageHall hall, Object msg);

    void publish(MessageHall hall, Collection<Object> msg);

}
