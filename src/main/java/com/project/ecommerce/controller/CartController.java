package com.project.ecommerce.controller;

import com.project.ecommerce.binder.InitBinderClass;
import com.project.ecommerce.dto.order.OrderItem;
import com.project.ecommerce.exceptions.CartNotInitialisedException;
import com.project.ecommerce.jsp_pages.JspPages;
import com.project.ecommerce.model.CartModel;
import com.project.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class CartController extends InitBinderClass {

    @Autowired
    CartService cartService;

    public static final String ITEMS="items";

    @RequestMapping("/addItem")
    public String addItem(@RequestParam(name = "itemId") String itemId,
                          Model model,
                          HttpServletRequest request
    ){

        Long itemIdLong = Long.parseLong(itemId);
        CartModel cartModel = (CartModel) request.getSession().getAttribute("cart");

        if (cartModel == null){
            throw new CartNotInitialisedException();
        }

        model.addAttribute(ITEMS, cartService.getItemsList());

        if (cartService.containsOrderItem(cartModel.getOrderItems(), itemIdLong)) {
            return JspPages.DASH_BOARD;
        }
        cartModel.addItem(cartService.getOrderItem(Long.parseLong(itemId)));

        model.addAttribute("cart",cartModel);

        return JspPages.DASH_BOARD;
    }

    @RequestMapping("/incrementItem")
    public String incrementItem(@RequestParam(name = "itemId") String itemId,
                                Model model,
                                HttpServletRequest request){
        Long itemIdLong = Long.parseLong(itemId);
        CartModel cartModel = (CartModel) request.getSession().getAttribute("cart");

        if (cartModel == null){
            throw new CartNotInitialisedException();
        }

        OrderItem orderItem = cartService.findOrderItem(cartModel.getOrderItems(), itemIdLong);

        orderItem.setQuantity(orderItem.getQuantity() + 1);
        cartModel.setTotalPrice(cartModel.getTotalPrice() + orderItem.getItem().getItemPrice());

        model.addAttribute("cart", cartModel);
        model.addAttribute(ITEMS, cartService.getItemsList());

        return JspPages.DASH_BOARD;
    }

    @RequestMapping("/decrementItem")
    public String decrementItem(@RequestParam(name = "itemId") String itemId,
                                Model model,
                                HttpServletRequest request){

        Long itemIdLong = Long.parseLong(itemId);
        CartModel cartModel = (CartModel) request.getSession().getAttribute("cart");

        if (cartModel == null){
            throw new CartNotInitialisedException();
        }

        OrderItem orderItem = cartService.findOrderItem(cartModel.getOrderItems(), itemIdLong);

        if (orderItem.getQuantity() > 1) {
            orderItem.setQuantity(orderItem.getQuantity() - 1);
            cartModel.setTotalPrice(cartModel.getTotalPrice() - orderItem.getItem().getItemPrice());
        }

        model.addAttribute("cart", cartModel);
        model.addAttribute(ITEMS, cartService.getItemsList());

        return JspPages.DASH_BOARD;
    }

    @RequestMapping("/deleteItem")
    public String deleteItem(@RequestParam(name = "itemId") String itemId,
                             Model model,
                             HttpServletRequest request){
        Long itemIdLong = Long.parseLong(itemId);
        CartModel cartModel = (CartModel) request.getSession().getAttribute("cart");

        if (cartModel == null){
            throw new CartNotInitialisedException();
        }

        OrderItem orderItem = cartService.findOrderItem(cartModel.getOrderItems(), itemIdLong);

        cartModel.removeItem(orderItem);

        model.addAttribute("cart", cartModel);
        model.addAttribute(ITEMS, cartService.getItemsList());

        return JspPages.DASH_BOARD;
    }
}
