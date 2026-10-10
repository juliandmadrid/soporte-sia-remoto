package com.carriez.flutter_hbb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Soporte SIA: el Agente SIA pregunta "¿estas vivo?" en cada latido (permiso de firma de la flota).
 * Se contesta con el ID SOLO si el servicio esta corriendo: si no, el silencio le dice al agente
 * que esta caido. Reemplaza al temporizador de 15 min, que se dormia con el procesador (Caeli, 10 oct).
 */
class PedirIdReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        if (MainService.corriendo) MainService.enviarId(ctx)
    }
}
