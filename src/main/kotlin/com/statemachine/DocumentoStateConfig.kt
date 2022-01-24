package com.statemachine

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.guard.Guard
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State
import java.util.*
import javax.swing.text.Document

@Configuration
@EnableStateMachine
class DocumentoStateConfig : EnumStateMachineConfigurerAdapter<DocumentoStates, DocumentoEvents>() {

    override fun configure(config: StateMachineConfigurationConfigurer<DocumentoStates, DocumentoEvents>) {
        with(config){
            withConfiguration()
                .autoStartup(true)
                .listener(getListener())
        }
    }

    override fun configure(states: StateMachineStateConfigurer<DocumentoStates, DocumentoEvents>) {
       states
            .withStates()
            .initial(DocumentoStates.RASCUNHO)
            .state(DocumentoStates.MODERACAO)
            .state(DocumentoStates.PUBLICADO, enviarEmail(), null)
    }



    override fun configure(transitions: StateMachineTransitionConfigurer<DocumentoStates, DocumentoEvents>) {
        transitions
             .withExternal()
                     .source(DocumentoStates.RASCUNHO).target(DocumentoStates.MODERACAO)
                     .event(DocumentoEvents.PUBLICADO_POR_USUARIO)
                     .guard(validarGuarda())
            .and()
                .withExternal()
                    .source(DocumentoStates.RASCUNHO).target(DocumentoStates.PUBLICADO)
                    .event(DocumentoEvents.PUBLICADO_POR_ADMINISTRACAO)
            .and()
                .withExternal()
                    .source(DocumentoStates.MODERACAO).target(DocumentoStates.PUBLICADO)
                    .event(DocumentoEvents.APROVADO)
            .and()
                .withExternal()
                     .source(DocumentoStates.MODERACAO).target(DocumentoStates.RASCUNHO)
                     .event(DocumentoEvents.REVISAO_OU_FALHA)
            .and()
                 .withExternal()
                     .source(DocumentoStates.PUBLICADO).target(DocumentoStates.RASCUNHO)
                     .event(DocumentoEvents.EXPIROU)
    }

    @Bean
    fun getListener() : StateMachineListenerAdapter<DocumentoStates, DocumentoEvents>{
        return StateMachineListenerAdapterImpl()
    }

    @Bean
    fun enviarEmail() = EnviarEmailAction()

    @Bean
    fun validarGuarda() = GuardaParaOEvento()
}

class EnviarEmailAction: Action<DocumentoStates, DocumentoEvents>{
    override fun execute(p0: StateContext<DocumentoStates, DocumentoEvents>?) {
        println("Enviando email quando o documento é publicado ")
    }
}

class StateMachineListenerAdapterImpl :  StateMachineListenerAdapter<DocumentoStates, DocumentoEvents>(){

    override fun stateChanged(
        from: State<DocumentoStates, DocumentoEvents>?,
        to: State<DocumentoStates, DocumentoEvents>?) {
        println("Estado do documento mudou de "+from?.id +" para "+to?.id)
    }
}

class GuardaParaOEvento:  Guard<DocumentoStates, DocumentoEvents> {
    override fun evaluate(p0: StateContext<DocumentoStates, DocumentoEvents>?): Boolean {
        print(p0?.message?.payload)
        return true
    }

}