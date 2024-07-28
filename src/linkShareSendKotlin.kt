import java.awt.BorderLayout
import java.awt.GridLayout
import java.io.PrintWriter
import java.net.Socket
import javax.swing.*

class linkShareSendKotlin{
    fun setPeerWriter(pw: PrintWriter?){
        peerWriter = pw
    }

    val myPort: Int
        get() = Companion.myPort

    companion object{
        private var peerWriter: PrintWriter? = null
        var iP = ""
        private var myPort = 4444
        @JvmStatic
        fun main(args: Array<String>){
            createFrame()
        }

        fun setPeerIP(ip: String){
            iP = ip
        }

        fun setPort(port: Int){
            myPort = port
        }

        fun connectPeer(ip: String?, port: Int){
            println("ran")
            try{
                val soc = Socket(ip, port)
                val pw = PrintWriter(soc.getOutputStream(), true)
                peerWriter = pw
            }catch(e: Exception){
                println(e)
            }
        }

        fun sendMessage(message: String?){
            peerWriter!!.println(message)
        }

        fun createFrame(){
            val frame = JFrame()
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.title = "LinkShareSend"
            val topPanel = JPanel(GridLayout(3, 2, 10, 0))
            val midPanel = JPanel(GridLayout(1, 1))
            val botPanel = JPanel(GridLayout(1, 1))
            val sendButton = JButton("Send")
            val connectButton = JButton("Connect")
            topPanel.add(JLabel("Peer IPv4:"))
            val ipTextField = JTextField("xxx.xxx.xx.xx", 10)
            topPanel.add(ipTextField)
            topPanel.add(JLabel("Port:"))
            val portTextField = JTextField("7777", 10)
            topPanel.add(portTextField)
            topPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            val textArea = JTextArea()
            textArea.lineWrap = true
            textArea.wrapStyleWord = true
            val scrollPane = JScrollPane(textArea)
            scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            midPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            connectButton.addActionListener{
                setPeerIP(ipTextField.text)
                setPort(portTextField.text.toInt())
                if(!iP.isEmpty()){
                    println("connecting...")
                    try{
                        connectPeer(iP, myPort)
                    }catch(err: Exception){
                        println("failed to connect")
                    }
                }else{
                    println("no ip entered")
                }
            }
            topPanel.add(connectButton)
            sendButton.addActionListener{
                sendMessage(textArea.text)
            }
            midPanel.add(scrollPane)
            botPanel.add(sendButton)
            botPanel.border = BorderFactory.createEmptyBorder(0, 10, 10, 10)
            val container = frame.contentPane
            container.add(topPanel, BorderLayout.NORTH)
            container.add(midPanel, BorderLayout.CENTER)
            container.add(botPanel, BorderLayout.SOUTH)
            frame.setSize(500, 350)
            frame.isVisible = true
        }
    }
}