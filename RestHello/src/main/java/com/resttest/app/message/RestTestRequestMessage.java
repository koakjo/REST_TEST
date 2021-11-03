package com.resttest.app.message;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestTestRequestMessage {
	
	@NonNull
	@Size(min=1)
	private String id;
	
	@Size(min=1)
	private String name;

}
