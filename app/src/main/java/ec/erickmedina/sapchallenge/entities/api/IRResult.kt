package ec.erickmedina.sapchallenge.entities.api

data class IRResult (var version: Int, var records: List<IRRecord>, var task_id: String, var status: IRStatus)