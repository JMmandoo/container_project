package pack.model.container;

import lombok.Data;

@Data
public class ContainerDto {
	private int  cont_price;
	private String cont_addr, cont_size, cont_image, owner_name, cont_status, business_num;
	private String  cont_name,  owner_phone, owner_num, cont_area, cont_explain, cont_no;
	private double cont_we, cont_kyung;

}
