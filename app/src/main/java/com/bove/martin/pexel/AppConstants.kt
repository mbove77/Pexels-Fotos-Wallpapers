package com.bove.martin.pexel

/**
 * Created by Martín Bove on 02/06/2018.
 * E-mail: mbove77@gmail.com
 */
object AppConstants {
    const val BASE_URL = "https://api.pexels.com/v1/"
    const val PEXELS_URL = "https://www.pexels.com/"
    const val PHOTO_URL = "large"
    const val LARGE_FOTO_URL = "large"
    const val PHOTOGRAPHER_NAME = "photographer_name"
    const val PHOTOGRAPHER_URL = "photo_url"
    const val ITEM_NUMBER = 15
    const val LANDSCAPE_ROW_COUNT = 3
    const val PORTRAIT_ROW_COUNT = 2
    const val IMAGES_FOLDER_NAME = "TEMP_PEXEL_FOLDER"

    enum class AppErrors {
        CONNECTION_ERROR { override fun getErrorMessage() = "Fallo la conexión con el servidor." },
        LOAD_IMAGE_ERROR { override fun getErrorMessage() = "Error al cargar la imagen."},
        PAGING_ERROR { override fun getErrorMessage() = "El número de página debe ser mayor a 0" },
        PHOTO_URL_ERROR { override fun getErrorMessage() = "La url de la foto es invalida." },
        QUERY_STRING_ERROR { override fun getErrorMessage() = "Fallo al buscar fotos." },
        SET_WALLPAPER_ERROR { override fun getErrorMessage() = "Error al cambiar el wallpaper en el dispositivo." },
        SHARE_PERMISSION_ERROR { override fun getErrorMessage() = "Necesitamos los permisos para compartir la imagen." },
        SET_LOCKSCREEN_ERROR { override fun getErrorMessage() = "Error al cambiar el wallpaper en el dispositivo." },
        LOCKSCREEN_API_VERSION_ERROR { override fun getErrorMessage() = "Disculpa, tu dispositivo no soporta esta función." },;

        abstract fun getErrorMessage(): String
    }

    enum class AppMessages {
        FILE_DOWNLOADED { override fun getMessage() = "Su foto se guardado con éxito" },
        WALLPAPER_CHANGE {override fun getMessage() = "Wallpaper cambiado" },;
        abstract fun getMessage(): String
    }
}