package com.ntd.dto.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ntd.dto.OrderDTO;
import com.ntd.dto.PostalAddressDTO;
import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductReviewDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.ReportDTO;
import com.ntd.dto.UserDTO;
import com.ntd.persistence.Order;
import com.ntd.persistence.PostalAddress;
import com.ntd.persistence.Product;
import com.ntd.persistence.ProductCategory;
import com.ntd.persistence.ProductReview;
import com.ntd.persistence.ProductSold;
import com.ntd.persistence.Report;
import com.ntd.persistence.User;

/**
 * Mapear DTO
 */
@Mapper(imports = { com.ntd.security.UserRole.class, java.util.Arrays.class })
public interface DTOMapperI {

	/** Dependencia de DTOMapper */
	public static final DTOMapperI MAPPER = Mappers.getMapper(DTOMapperI.class);

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
	 * Mapear Reporte a DTO
	 * 
	 * @param report
	 * @return ReportDTO
	 */
	@Mapping(target = "reviewDto", source = "review")
	@Mapping(target = "reporterDto", source = "reporter")
	public ReportDTO mapReportToDTO(Report report);

	/**
	 * Mapear DTO a Reporte
	 * 
	 * @param reportDTO
	 * @return Report
	 */
	@Mapping(target = "review", source = "reviewDto")
	@Mapping(target = "reporter", source = "reporterDto")
	public Report mapDTOToReport(ReportDTO reportDto);

	/**
	 * Mapear ProductReview a DTO
	 * 
	 * @param productReview
	 * @return ProductReviewDTO
	 */
	@Mapping(target = "userDto", ignore = true)
	@Mapping(target = "productDto", ignore = true)
	public ProductReviewDTO mapProductReviewToDTO(ProductReview productReview);

	/**
	 * Mapear DTO a ProductReview
	 * 
	 * @param productReviewDto
	 * @return ProductReview
	 */
	@Mapping(target = "user", source = "userDto")
	@Mapping(target = "product", source = "productDto")
	@Mapping(target = "reports", ignore = true)
	public ProductReview mapDTOToProductReview(ProductReviewDTO productReviewDto);

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
	@Mapping(target = "categoryId", source = "productCategory.categoryId")
	@Mapping(target = "reviewsDto", expression = "java(listProductReviewToDTO(product.getReviews()))")
	public ProductDTO mapProductToDTO(Product product);

	/**
	 * Mapear ProductCategory a DTO
	 * 
	 * @param productCategory
	 * @return ProductCategoryDTO
	 */
	public ProductCategoryDTO mapProductCategoryToDTO(ProductCategory productCategory);

	/**
	 * Mapear DTO a ProductCategory
	 * 
	 * @param productCategoryDto
	 * @return ProductCategory
	 */
	public ProductCategory mapDTOtoProductCategory(ProductCategoryDTO productCategoryDto);

	/**
	 * Mapear lista de DTO a ProductReview
	 * 
	 * @param reviews
	 * @return List
	 */
	default List<ProductReviewDTO> listProductReviewToDTO(List<ProductReview> reviews) {
		List<ProductReviewDTO> reviewsDto = new ArrayList<>();

		// Comprobar nulidad
		if (reviews != null) {

			// Mapear datos
			for (ProductReview review : reviews) {
				reviewsDto.add(mapProductReviewToDTO(review));
			}
		}

		return reviewsDto;
	}

	/**
	 * Mapear DTO a Product
	 * 
	 * @param productDto
	 * @return Product
	 */
	@Mapping(target = "soldProducts", ignore = true)
	@Mapping(target = "productCategory.categoryId", source = "categoryId")
	@Mapping(target = "reviews", ignore = true)
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
	@Mapping(target = "ordersDto", expression = "java(listOrderToDTO(user.getOrders()))")
	@Mapping(target = "role", expression = "java(user.getRole()==UserRole.BUYER ? 2 : 1)")
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
	 * Mapear DTO a User
	 * 
	 * @param userDto
	 * @return User
	 */
	@Mapping(target = "reportedReviews", ignore = true)
	@Mapping(target = "addresses", expression = "java(dtoToListPostalAddress(userDto.addressesDto()))")
	@Mapping(target = "orders", expression = "java(dtoToListOrder(userDto.ordersDto()))")
	@Mapping(target = "role", expression = "java(userDto.role()==2 ? UserRole.BUYER : UserRole.SELLER)")
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

}
