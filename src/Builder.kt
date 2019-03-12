import java.io.File

class Dialog {
    fun showTitle() = println("showing title")
    fun setTitle(title: String) = println("setting title text $title")
    fun setTitleColor(color: String) = println("setting title color $color")
    fun showMessage() = println("showing message")
    fun setMessage(text: String) = println("setting message $text")
    fun setMessageColor(color: String) = println("setting message color $color")
    fun showImage(bitmapBytes: ByteArray) = println("showing image with size ${bitmapBytes.size}")
    fun show() = println("showing dialog $this")
}

class DialogBuilder(init: DialogBuilder.() -> Unit) {

    init {
        init()
    }

    private var titleHolder: TextView? = null
    private var messageHolder: TextView? = null
    private var imageHolder: File? = null

    fun title(init: TextView.() -> Unit){
        titleHolder = TextView().apply { init() }
    }

    fun message(init: TextView.() -> Unit){
        messageHolder = TextView().apply { init() }
    }

    fun image(init: () -> File){
        imageHolder = init()
    }

    fun build(): Dialog {
        val dialog = Dialog()

        titleHolder?.apply {
            dialog.setTitle(text)
            dialog.setTitleColor(color)
            dialog.showTitle()
        }

        messageHolder?.apply {
            dialog.setMessage(text)
            dialog.setMessageColor(color)
            dialog.showMessage()
        }

        imageHolder?.apply {
            dialog.showImage(readBytes())
        }

        return dialog
    }

}

class TextView {
    var text: String = ""
    var color: String = "#00000"
}

fun main(args: Array<String>){

    val dialog = DialogBuilder{
        title {
            text = "Dialog Title"
        }
        message {
            text = "Dialog Message"
            color = "#333333"
        }
        image {
            File.createTempFile("image", "jpg")
        }
    }.build()

    print(dialog.show())

}