package com.jesse.sickstech.data.printer

import android.graphics.Bitmap

class EscPosProcessor {

    fun decodeBitmap(bmp: Bitmap): ByteArray {
        val width = bmp.width
        val height = bmp.height
        val pixels = IntArray(width * height)
        bmp.getPixels(pixels, 0, width, 0, 0, width, height)

        val res = mutableListOf<Byte>()
        // Comando para densidade de bits (ESC * m nL nH)
        // m = 33 (24-dot double density), nL e nH definem a largura
        val nL = (width % 256).toByte()
        val nH = (width / 256).toByte()

        var row = 0
        while (row < height) {
            res.add(0x1B)
            res.add(0x2A)
            res.add(33) // modo 24-dot
            res.add(nL)
            res.add(nH)

            for (col in 0 until width) {
                for (k in 0 until 3) { // 24 bits verticalmente (3 bytes)
                    var slice: Byte = 0
                    for (bit in 0 until 8) {
                        val y = row + k * 8 + bit
                        if (y < height) {
                            val p = pixels[y * width + col]
                            val gray = (0.299 * (p shr 16 and 0xFF) + 0.587 * (p shr 8 and 0xFF) + 0.114 * (p and 0xFF))
                            if (gray < 60) {
                                slice = (slice.toInt() or (1 shl (7 - bit))).toByte()
                            }
                        }
                    }
                    res.add(slice)
                }
            }
            res.add(0x0A) // Quebra de linha
            row += 24
        }
        return res.toByteArray()
    }


    fun obterTextoMockEmBytes(): ByteArray {
        val texto = StringBuilder()

        val alinharEsquerda = byteArrayOf(0x1B, 0x61, 0x00)
        val alinharCentro = byteArrayOf(0x1B, 0x61, 0x01)
        val negritoLigado = byteArrayOf(0x1B, 0x45, 0x01)
        val negritoDesligado = byteArrayOf(0x1B, 0x45, 0x00)

        val cmds = mutableListOf<Byte>()

        fun addText(t: String) {
            cmds.addAll(t.toByteArray().toList())
        }
        fun addCmd(vararg c: Byte) {
            cmds.addAll(c.toList())
        }

        addCmd(*alinharEsquerda)
        addText("TAQUARA/RJ/BRASIL\n")
        addText("CNPJ: 69.034.668/0001-56\n")
        addText("Numero de Serie: 68824014\n")
        addText("STONE - CAMISAS GALLEGO\n\n")


        addCmd(*alinharCentro)
        addCmd(*negritoLigado)
        addText("Relatorio Detalhado\n")
        addCmd(*negritoDesligado)
        addText("--------------------------------\n")
        addText("Resumo\n")
        addText("--------------------------------\n")


        addCmd(*alinharEsquerda)
        addText("De: 05/02/2019 as 00:00\n")
        addText("Ate: 05/04/2019 as 20:15\n\n")


        addCmd(*negritoLigado)
        addText("Valor Total:           R$ 1500.00\n")
        addCmd(*negritoDesligado)
        addText("Aprovadas:   120       R$ 1500.00\n")
        addText("Canceladas:  0         R$ 0.00\n\n")

        addText("Debito:                R$ 800.00\n")
        addText("Credito:               R$ 600.00\n")
        addText("Voucher:               R$ 100.00\n")


        addText("\n\n\n")

        return cmds.toByteArray()
    }

    fun textToBytes(text: String): ByteArray {
        val alinharEsquerda = byteArrayOf(0x1B, 0x61, 0x00)
        val alinharCentro = byteArrayOf(0x1B, 0x61, 0x01)
        val negritoLigado = byteArrayOf(0x1B, 0x45, 0x01)
        val negritoDesligado = byteArrayOf(0x1B, 0x45, 0x00)

        val cmds = mutableListOf<Byte>()

        fun addCmd(vararg c: Byte) { cmds.addAll(c.toList()) }

        fun addText(t: String) { cmds.addAll(t.toByteArray(Charsets.UTF_8).toList()) }

        addCmd(*alinharCentro)
        addCmd(*negritoLigado)
        addText("Jesse Burguer\n")
        addCmd(*negritoDesligado)

        addCmd(*alinharEsquerda)
        addText(text)

        addText("\n\n\n\n") // Avanço seguro

        return cmds.toByteArray()
    }




}