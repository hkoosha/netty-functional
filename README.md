This library allows you to create netty handlers in a functional manne instead 
of implementing a full fledged channel handler or extending an adapter. [![Build Status](https://travis-ci.org/hkoosha/netty-functional.svg?branch=master)](https://travis-ci.org/hkoosha/netty-functional)

## Maven

```xml
<dependency>
    <groupId>io.koosha.nettyfunctional</groupId>
    <artifactId>nettyfunctional</artifactId>
    <version>1.0.0</version>
</dependency>
```

```groovy
compile group: 'io.koosha.nettyfunctional', name: 'nettyfunctional', version: '1.0.0'
```

### How to Build

In the project directory run:

`./gradlew clean build`


### Example

```java

     channel -> channel
                .pipeline()
                .addLast(new HttpRequestEncoder())
                .addLast(new HttpResponseDecoder())
                .addLast(Inbound.hook(HttpResponse.class, (ctx, response) -> {
                    // response is of type HttpResponse.
                    // this is equivalent to implementing channelRead(...)
                    if(response.status().code != 200) {
                        log.error("did not get an OK reponse, closing {}", ctx.channel());
                        ctx.close();
                    }
                }))
                .addLast(Outbound.transform(String.class, (ctx, msg, p) -> {
                    // custom is of type String.
                    // this is equivalent to implementing write(...), processing
                    // message (transform it) and fire the result in outbound
                    // direction through pipeline.
                    return ctx.alloc()
                              .buffer(msg.length()) 
                              .writeBytes(msg.getBytes());
                }))
                .addLast(Event.rmSink(UserEvent.class, (ctx, event) -> {
                    // This handler catches user events of type UserEvent,
                    // but does NOT fire the event through the pipeline (sinks the event), 
                    // then removes itself (the rm part of the rmSink).
                    log.info("Hey! we got a: {}", event);
                }))
                ;
```


The code above is same as implementing three top level classes, or three verbose
inner anonymous classes.

Refactoring is easy, for instance Event.rmsink(...) can easily be written as a
top level class by extending RemovedEventSink:

```java

class MyEventHandler extends RemovedEventSink<T>(matcher) {

    @Override
    protected void event2(final ChannelHandlerContext ctx, final T event) throws Exception {

          log.info("Hey! we got a: {}", event);
    }
};
```


If later you decide you don't want to sink the event, or sink it conditionally,
and never remove your handler, going one step back again you can change 
`RemvoedEventSink` to `EventHook`, and implement the sink logic yourself. 
(Although you can sink conditionally already by extending `EventTransformer` and
return null as a flag to sink).



### Documentation? Unit Tests?

The project was accidentally removed by a `rm -rf ./*`, and the code is result
of decompiling and refactoring. I have to re-write unit tests and documentations
again.


### Warning:

Because of what was said above, this code is in pre-alpha stage.
I just know it compiles, and a handful of stuff works without a problem in my
own toy projects.
