package com.g3.spot_guide.base

interface IHasUploadModel {
    fun toUploadModel(): HashMap<String, Any?>
}