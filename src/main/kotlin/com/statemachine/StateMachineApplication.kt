package com.statemachine

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.statemachine.StateMachine


@SpringBootApplication
class StateMachineApplication (val stateMachine: StateMachine<DocumentoStates, DocumentoEvents>): CommandLineRunner {

	val logger: Logger = LoggerFactory.getLogger(StateMachineApplication::class.java)

	override fun run(vararg args: String?) {
		logger.info("Iniciando Testes")
		stateMachine.sendEvent(DocumentoEvents.PUBLICADO_POR_USUARIO)
		stateMachine.sendEvent(DocumentoEvents.APROVADO)

	}
}

fun main(args: Array<String>) {
	runApplication<StateMachineApplication>(*args)
}
