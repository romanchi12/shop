package org.romanchi.services;

import org.romanchi.Wired;
import org.romanchi.database.OrderStatus;
import org.romanchi.database.dao.OrderDao;
import org.romanchi.database.dao.OrderItemDao;
import org.romanchi.database.dto.OrderItemDTO;
import org.romanchi.database.entities.Order;
import org.romanchi.database.entities.OrderItem;
import org.romanchi.database.entities.Product;
import org.romanchi.database.entities.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

public class OrderService {

    @Wired
    private
    OrderDao orderDao;

    @Wired
    private
    OrderItemDao orderItemDao;

    @Wired
    private
    Logger logger;

    public OrderItem findOrderItemByProductAndOrder(Product product, Order order){
        Optional<OrderItem> orderItemOptional = orderItemDao.
                findByOrderIdAndProductId(order.getOrderId(),product.getProductId());
        return orderItemOptional.orElse(null);
    }

    public Order findOpenedOrderByUserId(long userId) {
        Iterable<Order> orderIterable = orderDao.findAllByUserId(userId);
        Order orderOpened = null;
        for(Order order:orderIterable){
            if(order.getOrderStatus()==OrderStatus.OPENED){
                orderOpened=order;
            }
        }
        return orderOpened;
    }

    public List<OrderItemDTO> getOrderItemsDTOsByOrderId(long orderId) {
        Iterable<OrderItem> orderItemIterable = orderItemDao.findAllByOrderId(orderId);
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        orderItemIterable.forEach(orderItem -> {
            logger.info(orderItem.toString());
            Product product = orderItem.getProduct();

            OrderItemDTO orderItemDTO = new OrderItemDTO();

            orderItemDTO.setOrderId(orderItem.getOrder().getOrderId());
            orderItemDTO.setSummaryPrice(orderItem.getOrder().getSummaryPrice());
            orderItemDTO.setOrderItemQuantity(orderItem.getOrderItemQuantity());
            orderItemDTO.setProductName(product.getProductName());
            orderItemDTO.setProductDescription(product.getProductDescription());
            orderItemDTO.setProductImageSrc(product.getProductImgSrc());
            orderItemDTO.setProductPrice(product.getProductPrice());
            orderItemDTO.setProductId(product.getProductId());
            orderItemDTOList.add(orderItemDTO);
        });
        return orderItemDTOList;
    }

    public long saveOrder(Order order) {
        return orderDao.save(order);
    }

    public String sendEmail(User user, long orderId) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("romanchi40160@gmail.com","frdfhtkm13");
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("romanchi40160@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getUserEmail()));
            message.setSubject("ESHOP New order");
            message.setText("Dear "+user.getUserName()+" " +user.getUserSurname()+"," +
                    "\n\n your order id: " + orderId);

            Transport.send(message);
            return "Message has been sent to " + user.getUserEmail();
        } catch (MessagingException e) {
            logger.severe(e.getLocalizedMessage());
            return "Problems with sending message";
        }
    }

    public void saveOrderItem(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }

    public int deleteOrderItem(OrderItem orderItem) {
        Optional<OrderItem> orderItemOptional = orderItemDao.findByOrderIdAndProductId(
                        orderItem.getOrder().getOrderId(),
                        orderItem.getProduct().getProductId());
        if(orderItemOptional.isPresent()){
            orderItemDao.delete(orderItemOptional.get());
            return 1;//"{'status':'ok'}"
        }else{
            return 2;//"{'status':'error', 'errorMessage':'No such order item'}"
        }
    }

    public List<Order> findOrdersByUser(User user) {
        Iterable<Order> orderIterable = orderDao.findAllByUserId(user.getUserId());
        List<Order> orders = new ArrayList<>();
        orderIterable.forEach(orders::add);
        return orders;
    }
}
