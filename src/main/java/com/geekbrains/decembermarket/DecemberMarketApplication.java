package com.geekbrains.decembermarket;

import com.geekbrains.decembermarket.beans.OrderListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DecemberMarketApplication {
    // (!!!! НЕ ДЕЛАТЬ !!!! )Добавлю к следующему занятию:
	// 2. Добавить регистрацию с валидацией
	// 4. Автоподвязывание по номеру телефона истории заказов (если человек на номер 123
	// в виде гостя оформил 20 заказов, и потом зарегался под этим номером, то под его
	// новую учетную запись должны подвязаться все эти заказы)
	// 1. Добавить платежную систему (PayPal)
	// 9. Картинки для товаров
	// 2. История просмотров товара (перехода на страницу товара) (куки) (внизу страницы линки на 5 последних просмотренных товаров)
	// Рассказать про почту

	// Домашнее задание:
	// - Добавьте SOAP сервис для магазина на получение информации о списке продуктов

	// План на курс:
	// 2. Авторизация через соцсети
	// 4. Отправка уведомлений пользователю, на сайте, или на почту
	// 5. Промокоды
	// 6. Логирование
	// 7. Профиль/редактирование профиля
	// 8. Отдельная админка
	// 10. HTTPS
	// 12. Статистика для владельца
	// 13. История действий на сайте
	// 14. Смс сервис
	// 15. Восстановление пароля
	// 16. Формирование PDF для заказа

	public static final String orderConfirmQueue = "order-confirm-queue";
	public static final String orderConfirmed = "order-confirmed-queue";
	public static final String orderConfirmExchange = "order-exchange";

	@Bean
	Queue orderConfirmQueue() {
		return new Queue(orderConfirmQueue, false);
	}

	@Bean
	Queue orderConfirmedQueue() {
		return new Queue(orderConfirmed, false);
	}

	@Bean
	TopicExchange orderExchange() {
		return new TopicExchange(orderConfirmExchange);
	}

	@Bean
	Binding bindingTopicOut(@Qualifier("orderConfirmQueue") Queue queue, TopicExchange orderExchange) {
		return BindingBuilder.bind(queue).to(orderExchange).with("order.confirm");
	}

	@Bean
	Binding bindingTopicIn(@Qualifier("orderConfirmedQueue") Queue queue, TopicExchange orderExchange) {
		return BindingBuilder.bind(queue).to(orderExchange).with("order.confirmed");
	}


	@Bean
	SimpleMessageListenerContainer containerForTopic(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("order-confirmed-queue");
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(OrderListener orderListener) {
		return new MessageListenerAdapter(orderListener, "receiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(DecemberMarketApplication.class, args);
	}
}
