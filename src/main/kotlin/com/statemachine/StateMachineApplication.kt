package com.statemachine

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.statemachine.StateMachine
import java.util.HashMap
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


@SpringBootApplication
class StateMachineApplication (val stateMachine: StateMachine<DocumentoStates, DocumentoEvents>): CommandLineRunner {

	val logger: Logger = LoggerFactory.getLogger(StateMachineApplication::class.java)

	override fun run(vararg args: String?) {
		logger.info("Iniciando Máquina de Estados")

		with(stateMachine){
			sendEvent(DocumentoEvents.PUBLICADO_POR_USUARIO)
			sendEvent(ConfigurarMessage("JOSE"))
			sendEvent(DocumentoEvents.APROVADO)
			sendEvent(DocumentoEvents.EXPIROU)
		}

		logger.info("Fim da Máquina de Estados")
	}
}

class ConfigurarMessage (val usuario: String)  : Message<DocumentoEvents>{

	override fun getPayload() = DocumentoEvents.PUBLICADO_POR_USUARIO

	override fun getHeaders(): MessageHeaders {
		var parametros = HashMap<String, Any>()
		parametros.put("usuario", this.usuario)
		return MessageHeaders(parametros);
	}
}

fun main(args: Array<String>) {
	runApplication<StateMachineApplication>(*args)
}
