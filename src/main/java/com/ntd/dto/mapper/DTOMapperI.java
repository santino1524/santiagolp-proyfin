package com.ntd.dto.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ntd.dto.CardDTO;
import com.ntd.dto.OrderDTO;
import com.ntd.dto.PostalAddressDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.UserDTO;
import com.ntd.persistence.Card;
import com.ntd.persistence.Order;
import com.ntd.persistence.PostalAddress;
import com.ntd.persistence.Product;
import com.ntd.persistence.ProductSold;
import com.ntd.persistence.User;

/**
 * Mapear DTO
 */
@Mapper
public interface DTOMapperI {

	/** Dependencia de DTOMapper */
	public static final DTOMapperI MAPPER = Mappers.getMapper(DTOMapperI.class);

	/**
	 * Mapear Tarjeta a DTO
	 * 
	 * @param card
	 * @return CardDTO
	 */
	@Mapping(target = "userDto", ignore = true)
	public CardDTO mapCardToDTO(Card card);

	/**
	 * Mapear DTO a Tarjeta
	 * 
	 * @param cardDto
	 * @return Card
	 */
	@Mapping(target = "user", source = "userDto")
	public Card mapDTOToCard(CardDTO cardDto);

	/**
	 * Mapear Producto vendido a DTO
	 * 
	 * @param productSold
	 * @return ProductSoldDTO
	 */
	@Mapping(target = "orderDto", ignore = true)
	@Mapping(target = "productDto", source = "product")
	public ProductSoldDTO mapProductSoldToDTO(ProductSold productSold);

	/**
	 * Mapear DTO a Producto vendido
	 * 
	 * @param productSoldDto
	 * @return ProductSold
	 */
	@Mapping(target = "order", source = "orderDto")
	@Mapping(target = "product", source = "productDto")
	public ProductSold mapDTOToProductSold(ProductSoldDTO productSoldDto);

	/**
	 * Mapear Order a DTO
	 * 
	 * @param order
	 * @return OrderDTO
	 */
	@Mapping(target = "userDto", ignore = true)
	@Mapping(target = "soldProductsDto", expression = "java(listProductSoldToDTO(order.getSoldProducts()))")
	public OrderDTO mapOrderToDTO(Order order);

	/**
	 * Mapear lista de ProductSold a DTO
	 * 
	 * @param soldProducts
	 * @return List
	 */
	default List<ProductSoldDTO> listProductSoldToDTO(List<ProductSold> soldProducts) {
		List<ProductSoldDTO> soldProductsDto = new ArrayList<>();

		// Comprobar nulidad
		if (soldProducts != null) {

			// Mapear datos
			for (ProductSold productSold : soldProducts) {
				soldProductsDto.add(mapProductSoldToDTO(productSold));
			}
		}

		return soldProductsDto;
	}

	/**
	 * Mapear DTO a Order
	 * 
	 * @param orderDto
	 * @return Order
	 */
	@Mapping(target = "user", source = "userDto")
	@Mapping(target = "soldProducts", expression = "java(dtoToListProductSold(orderDto.soldProductsDto()))")
	public Order mapDTOToOrder(OrderDTO orderDto);

	/**
	 * Mapear lista de DTO a ProductSold
	 * 
	 * @param soldProductsDto
	 * @return List
	 */
	default List<ProductSold> dtoToListProductSold(List<ProductSoldDTO> soldProductsDto) {
		List<ProductSold> soldProducts = new ArrayList<>();

		// Comprobar nulidad
		if (soldProductsDto != null) {

			// Mapear datos
			for (ProductSoldDTO productSoldDto : soldProductsDto) {
				soldProducts.add(mapDTOToProductSold(productSoldDto));
			}
		}

		return soldProducts;
	}

	/**
	 * Mapear PostalAddress a DTO
	 * 
	 * @param postalAddress
	 * @return PostalAddressDTO
	 */
	@Mapping(target = "directionLine", source = "addressId.directionLine")
	@Mapping(target = "city", source = "addressId.city")
	@Mapping(target = "province", source = "addressId.province")
	public PostalAddressDTO mapPostalAddressToDTO(PostalAddress postalAddress);

	/**
	 * Mapear DTO a PostalAddress
	 * 
	 * @param postalAddressDto
	 * @return PostalAddress
	 */
	@Mapping(target = "users", ignore = true)
	@Mapping(source = "directionLine", target = "addressId.directionLine")
	@Mapping(source = "city", target = "addressId.city")
	@Mapping(source = "province", target = "addressId.province")
	@Mapping(target = "addressId", ignore = true)
	public PostalAddress mapDTOToPostalAddress(PostalAddressDTO postalAddressDto);

	/**
	 * Mapear Product a DTO
	 * 
	 * @param product
	 * @return ProductDTO
	 */
	public ProductDTO mapProductToDTO(Product product);

	/**
	 * Mapear DTO a Product
	 * 
	 * @param productDto
	 * @return Product
	 */
	@Mapping(target = "soldProducts", ignore = true)
	@Mapping(target = "pvpPrice", expression = "java(calculatePvp(productDto))")
	public Product mapDTOToProduct(ProductDTO productDto);

	/**
	 * Calcular precio PVP
	 * 
	 * @param productDto
	 * @return BigDecimal
	 */
	default BigDecimal calculatePvp(ProductDTO productDto) {
		BigDecimal pvpPrice = new BigDecimal(0);

		// Comprobar nulidad
		if (productDto.basePrice() != null && productDto.iva() != null) {
			// Realizar calculo del precio de venta al publico
			pvpPrice = productDto.basePrice().multiply(productDto.iva().multiply(BigDecimal.valueOf(0.01)))
					.add(productDto.basePrice());
		}

		return pvpPrice;
	}

	/**
	 * Mapear User a DTO
	 * 
	 * @param user
	 * @return UserDTO
	 */
	@Mapping(target = "addressesDto", expression = "java(listPostalAddressToDTO(user.getAddresses()))")
	@Mapping(target = "cardsDto", expression = "java(listCardToDTO(user.getCards()))")
	@Mapping(target = "ordersDto", expression = "java(listOrderToDTO(user.getOrders()))")
	public UserDTO mapUserToDTO(User user);

	/**
	 * Mapear lista de DTO a Order
	 * 
	 * @param orders
	 * @return List
	 */
	default List<OrderDTO> listOrderToDTO(List<Order> orders) {
		List<OrderDTO> ordersDto = new ArrayList<>();

		// Comprobar nulidad
		if (orders != null) {

			// Mapear datos
			for (Order order : orders) {
				ordersDto.add(mapOrderToDTO(order));
			}
		}

		return ordersDto;
	}

	/**
	 * Mapear lista de DTO a PostalAddress
	 * 
	 * @param addresses
	 * @return List
	 */
	default List<PostalAddressDTO> listPostalAddressToDTO(List<PostalAddress> addresses) {
		List<PostalAddressDTO> addressesDto = new ArrayList<>();

		// Comprobar nulidad
		if (addresses != null) {

			// Mapear datos
			for (PostalAddress postalAddress : addresses) {
				addressesDto.add(mapPostalAddressToDTO(postalAddress));
			}
		}

		return addressesDto;
	}

	/**
	 * Mapear lista de DTO a Card
	 * 
	 * @param cards
	 * @return List
	 */
	default List<CardDTO> listCardToDTO(List<Card> cards) {
		List<CardDTO> cardsDto = new ArrayList<>();

		// Comprobar nulidad
		if (cards != null) {

			// Mapear datos
			for (Card card : cards) {
				cardsDto.add(mapCardToDTO(card));
			}
		}

		return cardsDto;
	}

	/**
	 * Mapear DTO a User
	 * 
	 * @param userDto
	 * @return User
	 */
	@Mapping(target = "addresses", expression = "java(dtoToListPostalAddress(userDto.addressesDto()))")
	@Mapping(target = "cards", expression = "java(dtoToListCard(userDto.cardsDto()))")
	@Mapping(target = "orders", expression = "java(dtoToListOrder(userDto.ordersDto()))")
	public User mapDTOToUser(UserDTO userDto);

	/**
	 * Mapear lista de Order a DTO
	 * 
	 * @param ordersDto
	 * @return List
	 */
	default List<Order> dtoToListOrder(List<OrderDTO> ordersDto) {
		List<Order> orders = new ArrayList<>();

		// Comprobar nulidad
		if (ordersDto != null) {

			// Mapear datos
			for (OrderDTO orderDto : ordersDto) {
				orders.add(mapDTOToOrder(orderDto));
			}
		}

		return orders;
	}

	/**
	 * Mapear lista de PostalAddress a DTO
	 * 
	 * @param addressesDto
	 * @return List
	 */
	default List<PostalAddress> dtoToListPostalAddress(List<PostalAddressDTO> addressesDto) {
		List<PostalAddress> addresses = new ArrayList<>();

		// Comprobar nulidad
		if (addressesDto != null) {

			// Mapear datos
			for (PostalAddressDTO postalAddressDto : addressesDto) {
				addresses.add(mapDTOToPostalAddress(postalAddressDto));
			}
		}

		return addresses;
	}

	/**
	 * Mapear lista de Card a DTO
	 * 
	 * @param cardsDto
	 * @return List
	 */
	default List<Card> dtoToListCard(List<CardDTO> cardsDto) {
		List<Card> cards = new ArrayList<>();

		// Comprobar nulidad
		if (cardsDto != null) {

			// Mapear datos
			for (CardDTO cardDto : cardsDto) {
				cards.add(mapDTOToCard(cardDto));
			}
		}

		return cards;
	}

}
