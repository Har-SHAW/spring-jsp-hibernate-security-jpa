package com.project.ecommerce.dto.order;

import com.project.ecommerce.dto.item.Item;
import com.project.ecommerce.entity.order.OrderItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class OrderItem implements Serializable {
    private Integer quantity;
    private Item item;

    public OrderItem(OrderItemEntity orderItemEntity){
        this.quantity = orderItemEntity.getQuantity();
        this.item = new Item(orderItemEntity.getItemEntity());
    }
}
