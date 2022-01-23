package com.statemachine

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State
import java.util.*

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
            .states(EnumSet.allOf(DocumentoStates::class.java))
    }

    override fun configure(transitions: StateMachineTransitionConfigurer<DocumentoStates, DocumentoEvents>) {
        transitions
             .withExternal()
                     .source(DocumentoStates.RASCUNHO).target(DocumentoStates.MODERACAO)
                     .event(DocumentoEvents.PUBLICADO_POR_USUARIO)
            .and()
                .withExternal()
                    .source(DocumentoStates.RASCUNHO).target(DocumentoStates.PUBLICADO)
                    .event(DocumentoEvents.PUBLICADO_POR_ADMINISTRACAO)
            .and()
                .withExternal()
                    .source(DocumentoStates.MODERACAO).target(DocumentoStates.PUBLICADO)
                    .event(DocumentoEvents.APROVADO)

    }

    @Bean
    fun getListener() : StateMachineListenerAdapter<DocumentoStates, DocumentoEvents>{
        return StateMachineListenerAdapterImpl()
    }
}

class StateMachineListenerAdapterImpl :  StateMachineListenerAdapter<DocumentoStates, DocumentoEvents>(){

    override fun stateChanged(
        from: State<DocumentoStates, DocumentoEvents>?,
        to: State<DocumentoStates, DocumentoEvents>?) {
        println("Estado do documento mudou de "+from?.id +" para "+to?.id)
    }
}