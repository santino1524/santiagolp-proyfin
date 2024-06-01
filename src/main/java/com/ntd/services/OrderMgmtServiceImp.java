package com.ntd.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.ntd.dto.OrderDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.dto.validators.OrderStatusValidator;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Order;
import com.ntd.persistence.OrderRepositoryI;
import com.ntd.persistence.PostalAddress;
import com.ntd.persistence.PostalAddressRepositoryI;
import com.ntd.persistence.ProductSold;
import com.ntd.persistence.ProductSoldRepositoryI;
import com.ntd.persistence.User;
import com.ntd.persistence.UserRepositoryI;
import com.ntd.utils.Constants;
import com.ntd.utils.DateUtil;
import com.ntd.utils.QRCode;
import com.ntd.utils.ValidateParams;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de pedidos
 * 
 * @author SLP
 */
@Service
@Slf4j
public class OrderMgmtServiceImp implements OrderMgmtServiceI {

	/** String Hola */
	private static final String HOLA = "Hola ";

	/** Dependencia de OrderRepository */
	private final OrderRepositoryI orderRepository;

	/** Dependencia de PostalAddressRepositoryI */
	private final PostalAddressRepositoryI addressRepository;

	/** Dependencia de ProductSoldRepository */
	private final ProductSoldRepositoryI productSoldRepository;

	/** Depenencia EmailService */
	private EmailMgmtServiceI emailService;

	/** Depenencia UserRepository */
	private UserRepositoryI userRepository;

	/**
	 * Constructor
	 * 
	 * @param orderRepository
	 * @param addressRepository
	 * @param productSoldRepository
	 * @param emailService
	 * @param userRepository
	 */
	public OrderMgmtServiceImp(OrderRepositoryI orderRepository, PostalAddressRepositoryI addressRepository,
			ProductSoldRepositoryI productSoldRepository, EmailMgmtServiceI emailService,
			UserRepositoryI userRepository) {
		this.orderRepository = orderRepository;
		this.addressRepository = addressRepository;
		this.productSoldRepository = productSoldRepository;
		this.emailService = emailService;
		this.userRepository = userRepository;
	}

	@Override
	public ResponseEntity<Object> insertOrder(OrderDTO orderDto) throws InternalException, MessagingException {
		if (log.isInfoEnabled())
			log.info("Insertar pedido");

		// Validar parametro
		ValidateParams.isNullObject(orderDto);

		// Extraer lista de productos vendidos
		List<ProductSold> soldProducts = (DTOMapperI.MAPPER.dtoToListProductSold(orderDto.soldProductsDto()));

		// Vaciar lista de productos vendidos para guardar pedido
		Order order = DTOMapperI.MAPPER.mapDTOToOrder(orderDto);
		order.getSoldProducts().clear();

		// Mapear DTO y guardar
		order = orderRepository.save(order);

		// Agregar order a soldProducts
		for (ProductSold productSold : soldProducts) {
			productSold.setOrder(order);
		}

		// Guardar lista de productos vendidos
		List<ProductSold> soldProductsRegistered = productSoldRepository.saveAll(soldProducts);

		ResponseEntity<Object> result;
		if (order != null && soldProductsRegistered.size() == soldProducts.size()) {
			// Retornar 200 para operacion exitosa
			result = ResponseEntity.ok().build();

			// Mandar correo de nuevo pedido
			User user = userRepository.findById(orderDto.userId()).orElseThrow(InternalException::new);

			// Preparar cuerpo del correo
			StringBuilder body = new StringBuilder();
			body.append(HOLA);
			body.append(user.getName());
			body.append(", ¡Gracias por tu reciente compra en la ");
			body.append(Constants.STORE_NAME);
			body.append("! Nos complace informarte que tu pedido ha sido recibido y está siendo procesado.");

			// Preparar asunto del correo
			StringBuilder subject = new StringBuilder();
			subject.append("¡Gracias por tu pedido! Detalles de tu compra en la ");
			subject.append(Constants.STORE_NAME);

			// Enviar correo
			emailService.sendEmail(user.getEmail(), subject.toString(), body.toString());
		} else {
			// Retornar 500 para error
			result = ResponseEntity.internalServerError().build();
		}

		// Retornar mensaje de operacion
		return result;

	}

	@Override
	public OrderDTO updateOrderStatus(Long orderId, String status) throws InternalException, MessagingException {
		if (log.isInfoEnabled())
			log.info("Actualizar estado de pedido");

		// Validar parametros
		ValidateParams.isNullObject(orderId);
		OrderStatusValidator orderStatusValid = new OrderStatusValidator();
		if (!orderStatusValid.isValid(status, null)) {
			throw new InternalException();
		}

		// Actualizar estado de pedido
		final Order order = orderRepository.findById(orderId).orElseThrow(InternalException::new);
		order.setStatus(status);

		// Obtener datos del usuario
		User user = userRepository.findById(order.getUser().getUserId()).orElseThrow(InternalException::new);

		if (status.equals(Constants.getOrderStatuses().get(2))) {
			// Mandar correo de cancelacion de pedido

			// Preparar cuerpo del correo
			StringBuilder body = new StringBuilder();
			body.append(HOLA);
			body.append(user.getName());
			body.append(", lamentamos informarte que tu pedido número: ");
			body.append(order.getOrderNumber());
			body.append(", realizado el ");
			body.append(DateUtil.formatDate(order.getOrderDate()));
			body.append(
					", ha sido cancelado. El monto total de tu pedido será reembolsado a través del mismo método de pago utilizado en la compra.");

			// Preparar asunto del correo
			StringBuilder subject = new StringBuilder();
			subject.append("Actualización sobre tu pedido en la ");
			subject.append(Constants.STORE_NAME);

			// Enviar correo
			emailService.sendEmail(user.getEmail(), subject.toString(), body.toString());

		} else if (status.equals(Constants.getOrderStatuses().get(1))) {
			// Mandar correo de pedido enviado

			// Preparar cuerpo del correo
			StringBuilder body = new StringBuilder();
			body.append(HOLA);
			body.append(user.getName());
			body.append(", nos complace informarte que tu pedido número: ");
			body.append(order.getOrderNumber());
			body.append(", realizado el ");
			body.append(DateUtil.formatDate(order.getOrderDate()));
			body.append(
					", ha sido enviado. Agradecemos tu confianza y esperamos que disfrutes de tu compra. No dudes en compartir tu experiencia con nosotros.");

			// Preparar asunto del correo
			StringBuilder subject = new StringBuilder();
			subject.append("¡Tu pedido en la ");
			subject.append(Constants.STORE_NAME);
			subject.append(" ha sido enviado!");

			// Enviar correo
			emailService.sendEmail(user.getEmail(), subject.toString(), body.toString());
		}

		// Persistir Pedido y retornar DTO
		return DTOMapperI.MAPPER.mapOrderToDTO(orderRepository.save(order));
	}

	@Override
	public void deleteOrder(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar pedido");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por DNI
		orderRepository.deleteById(id);
	}

	@Override
	public List<OrderDTO> searchAllOrders() {
		if (log.isInfoEnabled())
			log.info("Buscar todos los pedidos");

		// Buscar todos las tarjetas
		final List<Order> orders = orderRepository.findAll();

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public boolean existsOrder(String orderNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el numero de pedido");

		// Validar parametro
		ValidateParams.isNullObject(orderNumber);

		// Retornar si existe el pedido en BBDD
		return orderRepository.existsByOrderNumber(orderNumber);
	}

	@Override
	public List<OrderDTO> searchByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos entre fechas");

		// Validar parametros
		ValidateParams.isNullObject(startDate);
		ValidateParams.isNullObject(endDate);

		// Buscar pedidos
		final List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByOrderByOrderDateAsc() {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos ordenados ascendentemente por fecha");

		// Buscar por pedidos
		final List<Order> orders = orderRepository.findByOrderByOrderDateAsc();

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByOrderByOrderDateDesc() {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos ordenados descendentemente por fecha");

		// Buscar por pedidos
		final List<Order> orders = orderRepository.findByOrderByOrderDateDesc();

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public OrderDTO searchByOrderNumber(String orderNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero de pedido ");

		// Validar parametro
		ValidateParams.isNullObject(orderNumber);

		// Buscar por numero de pedido
		final Order order = orderRepository.findByOrderNumber(orderNumber);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapOrderToDTO(order);
	}

	@Override
	public List<OrderDTO> searchByStatus(String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos por estado");

		// Validar parametro
		ValidateParams.isNullObject(status);

		// Buscar por pedidos
		final List<Order> orders = orderRepository.findByStatusIgnoreCase(status);

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByUser(Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos por usuario");

		// Validar parametro
		ValidateParams.isNullObject(userId);

		// Buscar por usuario
		final List<Order> orders = orderRepository.findByUser(new User(userId, null, null, null, null, null, null, null,
				null, false, true, null, null, null, null, null));

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByOrderIdOrOrderNumber(Long orderId, String orderNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por id o numero de pedido");

		// Validar parametros
		ValidateParams.isNullObject(orderId);
		ValidateParams.isNullObject(orderNumber);

		// Buscar por id o numero de pedido
		final List<Order> orders = orderRepository.findByOrderIdOrOrderNumber(orderId, orderNumber);

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByUserOrderDateDesc(Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos por usuario por fecha desc");

		// Validar parametro
		ValidateParams.isNullObject(userId);

		// Buscar por usuario
		final List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(new User(userId, null, null, null,
				null, null, null, null, null, false, true, null, null, null, null, null));

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public OrderDTO searchTopByUser(Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ultimo pedido del usuario");

		// Validar parametro
		ValidateParams.isNullObject(userId);

		// Buscar por usuario
		final Order order = orderRepository.findTopByUserOrderByOrderDateDesc(new User(userId, null, null, null, null,
				null, null, null, null, false, true, null, null, null, null, null));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapOrderToDTO(order);
	}

	@Override
	public int countByStatusEquals(String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar cantidad de pedidos creados");

		// Validar parametro
		ValidateParams.isNullObject(status);

		return orderRepository.countByStatusEquals(status);
	}

	@Override
	public List<OrderDTO> findByStatusEquals(String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar los pedidos en estado 'CREADO'");

		// Validar parametro
		ValidateParams.isNullObject(status);

		return DTOMapperI.MAPPER.listOrderToDTO(orderRepository.findByStatusEqualsOrderByOrderDateAsc(status));
	}

	@Override
	public OrderDTO searchById(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedido por id");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapOrderToDTO(orderRepository.findById(id).orElse(null));
	}

	/**
	 * Generar etiqueta
	 * 
	 * @param order
	 * @return byte[]
	 */
	private byte[] generateShippingLabelPDF(Order order) {
		if (log.isInfoEnabled())
			log.info("Generar PDF");

		try {
			// Obtener el tamaño de pagina A4
			Rectangle a4PageSize = PageSize.A4;

			// Calcular la mitad del ancho y la mitad de la altura de A4
			float halfWidth = a4PageSize.getWidth() / 2;
			float halfHeight = a4PageSize.getHeight() / 2;

			// Crear un nuevo tamanno de pagina
			Rectangle customPageSize = new Rectangle(halfWidth, halfHeight);

			Document document = new Document(customPageSize);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			document.open();

			// Configurar el estilo para los encabezados en negrita
			com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
					12, com.itextpdf.text.Font.BOLD);

			// Configurar el estilo para el nombre de la tienda centrado
			Paragraph store = new Paragraph(Constants.STORE_NAME, new com.itextpdf.text.Font(
					com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 16, com.itextpdf.text.Font.BOLD));
			store.setAlignment(Element.ALIGN_CENTER);
			document.add(store);

			// Direccion del remitente
			List<PostalAddress> addresses = addressRepository.findByUser(new User(Long.valueOf(1), null, null, null,
					null, null, null, null, null, false, true, null, null, null, null, null));

			PostalAddress storePostalAddress = null;
			for (PostalAddress address : addresses) {
				if (address.getUser() != null) {
					storePostalAddress = address;
					break;
				}
			}

			StringBuilder storeAddress = new StringBuilder();
			storeAddress.append("Dirección: ");
			if (storePostalAddress != null && storePostalAddress.getAddressId() != null) {
				storeAddress.append(storePostalAddress.getDirectionLine());
				storeAddress.append(", ");
				storeAddress.append(storePostalAddress.getCity());
				storeAddress.append(", ");
				storeAddress.append(storePostalAddress.getProvince());
				storeAddress.append(", ");
				storeAddress.append(storePostalAddress.getCp());
				storeAddress.append(", ");
				storeAddress.append(storePostalAddress.getCountry());
			}

			document.add(new Paragraph(storeAddress.toString()));

			document.add(new com.itextpdf.text.Paragraph("\n"));

			// Encabezado y datos del cliente
			Paragraph datosClienteHeader = new Paragraph("Datos del cliente:", boldFont);
			document.add(datosClienteHeader);
			StringBuilder customer = new StringBuilder();
			customer.append("Nombre y apellidos: ");
			customer.append(order.getUser().getName());
			customer.append(" ");
			customer.append(order.getUser().getSurname());
			customer.append(" ");
			customer.append(order.getUser().getSecondSurname());
			customer.append(", DNI: ");
			customer.append(order.getUser().getDni());
			document.add(new Paragraph(customer.toString()));

			// Direccion de envio
			StringBuilder address = new StringBuilder();
			address.append("Dirección de envío: ");
			address.append(order.getShippingAddress().getDirectionLine());
			address.append(", ");
			address.append(order.getShippingAddress().getCity());
			address.append(", ");
			address.append(order.getShippingAddress().getProvince());
			address.append(", ");
			address.append(order.getShippingAddress().getCp());
			address.append(", ");
			address.append(order.getShippingAddress().getCountry());
			document.add(new Paragraph(address.toString()));

			document.add(new com.itextpdf.text.Paragraph("\n"));

			// Encabezado y datos del pedido
			Paragraph datosPedidoHeader = new Paragraph("Datos del pedido:", boldFont);
			document.add(datosPedidoHeader);
			document.add(new Paragraph("Número del pedido: " + order.getOrderNumber()));

			// Generar el codigo QR
			String productNumber = order.getOrderNumber();
			int qrCodeWidth = 120;
			int qrCodeHeight = 120;
			Image qrCodeImage = QRCode.generateQRCodeImage(productNumber, qrCodeWidth, qrCodeHeight);
			qrCodeImage.setAlignment(Element.ALIGN_CENTER);
			document.add(qrCodeImage);

			document.close();
			return baos.toByteArray();

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			return new byte[0];
		}
	}

	@Override
	public byte[] generateLabel(Long orderId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Generar etiqueta de envio");

		// Validar parametro
		ValidateParams.isNullObject(orderId);

		byte[] result = new byte[0];

		// Buscar pedido
		Order order = orderRepository.findById(orderId).orElse(new Order());

		if (order != null && order.getOrderId() != null) {
			result = generateShippingLabelPDF(order);
		}

		// Generar etiqueta
		return result;
	}
}
