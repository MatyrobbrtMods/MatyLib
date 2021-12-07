package com.matyrobbrt.lib.scheduler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public abstract class Scheduler<T> {

	public static final int QUEUE_OVERFLOW_LIMIT = 200;

	protected volatile Queue<Consumer<T>> scheduledActions = new ConcurrentLinkedDeque<>();

}
