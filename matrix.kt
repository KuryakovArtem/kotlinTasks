public class Cell {

    var real: Double
    var imaginary: Double

    constructor (real: Double, imaginary: Double) {
        this.real = real
        this.imaginary = imaginary
    }

    fun add(b: Cell) : Cell{
        return Cell(this.real + b.real, this.imaginary + b.imaginary)
    }

    fun multiply(b: Cell) : Cell{
        return Cell(this.real * b.real - this.imaginary * b.imaginary, this.imaginary * b.real + this.real * b.imaginary)
    }

    override fun toString(): String {
        var res : String = this.real.toBigDecimal().toPlainString() + ' ' + this.imaginary.toBigDecimal().toPlainString() + 'i'
        return res
    }
}

public class Matrix{
    var body: Array<Array<Cell>>
    var cols: Int
    var rows: Int

    constructor (rows: Int, cols: Int) {
        this.body = Array(rows, { Array(cols, {Cell(0.0, 0.0)}) })
        this.cols= cols
        this.rows = rows
    }

    fun set(row: Int, col: Int, value: Cell){
        this.body[row][col] = value
    }

    fun get(row: Int, col: Int) : Cell
    {
        return body[row][col]
    }

    override fun toString(): String {
        var res : String = ""
        for (row in this.body){
            for (elem in row)
            {
                res += elem.toString() + ", "
            }
            res += '\n'
        }
        return res
    }

    fun add(b: Matrix) : Matrix{
        if (this.cols != b.cols || this.rows != b.rows){
            return Matrix(1, 1)
        }

        var res : Matrix = Matrix(this.rows, this.cols)
        for (i in 0..(rows-1)){
            for (j in 0..(cols-1))
            {
                res.set(i, j, this.get(i,j).add(b.get(i,j)))
            }
        }
        return res
    }

    fun multiply(b: Matrix) : Matrix{
        if (this.cols != b.rows){
            return Matrix(1, 1)
        }

        var res : Matrix = Matrix(this.rows, b.cols)
        for (i in 0..(rows-1)){
            for (j in 0..(cols-1)){
                var elem: Cell = Cell(0.0, 0.0)
                for (t in 0..(this.cols - 1)){
                    elem = elem.add(this.get(i, t).multiply(b.get(t, j)));
                }
                res.set(i, j, elem)
            }
        }
        return res
    }

    fun trans() : Matrix{
        var res : Matrix = Matrix(this.cols, this.rows)
        for (i in 0..(rows-1)){
            for (j in 0..(cols-1)){
                res.set(j, i, this.get(i, j))
            }
        }
        return res
    }
}

fun main() {
    println("Hello, world!!!")

    var a: Matrix = Matrix(2, 2)
    a.set(0,0,Cell(1.0, 0.0))
    a.set(0,1,Cell(2.0, 0.0))
    a.set(1,0,Cell(3.0, 0.0))
    a.set(1,1,Cell(4.0, 0.0))

    var b: Matrix = Matrix(2, 2)
    b.set(0,0,Cell(4.0, 0.0))
    b.set(0,1,Cell(3.0, 0.0))
    b.set(1,0,Cell(2.0, 0.0))
    b.set(1,1,Cell(1.0, 0.0))

    println(a)
    println("--------")
    println(b)
    println("--------")
    println(a.add(b))
    println("--------")
    println(a.multiply(b))
    println("--------")
    println(a.trans())
    println("--------")
}