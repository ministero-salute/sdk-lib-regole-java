package it.mds.sdk.libreriaregole.dtos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public abstract class RecordDtoGenerico {

	private static final String GET_PREFIX = "get";

	private CampiInputBean campiInput;

	public Object getCampo(String nomeCampo) throws  IllegalAccessException,NoSuchMethodException, InvocationTargetException{

		Object valueObject = null;
		Method declaredMethod = null;
		StringBuilder builder = new StringBuilder();
		builder.append(GET_PREFIX);

		declaredMethod = this.getClass().getDeclaredMethod(builder.append(StringUtils.capitalize(nomeCampo)).toString(), null);
		valueObject = declaredMethod.invoke(this, null);

		return valueObject;
	}

	public CampiInputBean getCampiInput() {
		return campiInput;
	}

	public void setCampiInput(CampiInputBean campiInput) {
		this.campiInput = campiInput;
	}
}