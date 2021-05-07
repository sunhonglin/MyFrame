data class Person(var name: String? = null, val age: Int? = null)


val persons = mutableListOf(Person(name = "Alice"), Person(name = "Bob", age = 29))
val oldest = persons.maxByOrNull { it.age ?: 0 }// ?: Elvis运算符
println("The oldest is: $oldest") // The oldest is: Person(name=Bob, age=29)  -> data class 会自动生成toString 方法

/**
 * 函数式编程 - 简洁
 * 头等函数 - 把函数（一小段行为）当作值使用，可以用变量保存它，把它当做单数传递，或者当作其他函数的返回值。
 * 不可变形 - 使用不可变对象，这宝整理他们的状态在其创建之后不能再变换。
 * 无副作用 - 使用的事纯函数。
 * 假设有两段类似代码，实现相似的任务（例如，再集合中寻找匹配的元素）但具体细节略有不同（如何判断元素是匹配的）。
 * 可以轻易将逻辑中公共部分提取到一个函数中，将不同部分作为参数传递给他。这些参数本身也是函数，可以使用一种简洁的语法来表示这些匿名函数，被称作lambda表达式：
 * 好处：简洁、多线程安全、测试简单
 * 例，类find
 */
inline fun <T> Iterable<T>.findPerson(selector: (T) -> Boolean): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    do {
        val result = iterator.next()
        val v = selector(result)
        if (v) {
            return result
        }

    } while (iterator.hasNext())
    return null
}
println(persons.find { it.name == "Alice" })
println(persons.findPerson { it.name == "Alice" })
println(persons.findPerson { it.name == null })
println(persons.findPerson { it.name == "" })
println(persons.findPerson { it.name == "123" })

/**
 * 表达式函数体
 * 函数体写在花括号中，我们说这个函数有代码块体。如果直接返回了一个表达式，它就有表达式体。
 * option + 回车，可进行转换。
 */
fun max(a: Int, b: Int): Int = if (a > b) a else b
//表达式函数可省略返回类型
//fun max(a: Int, b: Int) = if (a > b) a else b

/**
 * 自定义访问器
 * 与声明无参数的函数一样。一版来说描述的是类的特征，声明为属性
 */
class Rectangle(val height: Int, val width: Int) {
    // 表达式体
    val isSquare: Boolean
        get() = height == width// 声明属性的getter

    //代码块体
//    val isSquare: Boolean
//        get() {
//            return height == width
//        }

    //声明无参数的函数
//    val isSquare = height == width
}
val rectangle = Rectangle(41, 43)
println(rectangle.isSquare)

/**
 * 字符串模板
 */
val names = arrayOf("Bob")
val name = if (names.isNotEmpty()) names[0] else "kotlin"
println("Hello $name")
val names1 = arrayOf<String>()
val name1 = if (names1.isNotEmpty()) names[0] else "kotlin"
println("Hello $name1")

/**
 * $转义
 */
println("\$")

/**
 * 枚举
 * 使用enum class关键字，enum为软关键字，只有出现在class前面时才有意义，其他地方可以当做普通的名称使用。
 */
enum class Color(val r: Int, val g: Int, val b: Int) {
    RED(255, 0, 0), ORANGE(255, 165, 0), YELLOW(255, 255, 0),
    GREEN(0, 255, 0), BLUE(0, 0, 255), INDIGO(75, 0, 130),
    VIOLET(238, 130, 238);// kotlin语法中唯一必须使用分号的地方，内含自定义方法

    fun rgb() = (r * 256 + g) * 256 + b
}
println(Color.RED.rgb())

/**
 * when + 枚举， when允许使用任何对象
 * 单选项
 */
fun getMnemonic(color: Color) =
    when (color) {
        Color.RED -> "Richard"
        Color.ORANGE -> "Of"
        Color.YELLOW -> "York"
        Color.GREEN -> "Gave"
        Color.BLUE -> "Battle"
        Color.INDIGO -> "In"
        Color.VIOLET -> "Vain"
    }
println(getMnemonic(Color.BLUE))

/**
 * 多选项，逗号隔开
 */
fun getWarmth(color: Color) =
    when (color) {
        Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
        Color.GREEN -> "neutral"
        else -> "cold"
    }
println(getWarmth(Color.BLUE))
// when可使用任何对象，次方式会重复创建set对象，影响效率
println(
    when (setOf(Color.BLUE, Color.YELLOW)) {
        setOf(Color.YELLOW, Color.BLUE) -> Color.ORANGE
        else -> "not found"
    }
)

/**
 * 智能转换
 */
interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr): Int {
    if (e is Num) {// is 与 java 中instanceOf相似，kotlin判断后无需转换
//        var a = e as Num // 显式转换
        return e.value// 智能转换后会用不同背景显示
    }
    if (e is Sum) {
        return eval(e.right) + eval(e.left)
    }
    throw IllegalArgumentException("Unknown expression")
}
println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))
fun evalWithLogging(e: Expr): Int =
    when (e) {
        is Num -> {
            println("num: ${e.value}")
            e.value
        }
        is Sum -> {
            val left = evalWithLogging(e.left)
            val right = evalWithLogging(e.right)
            println("Sum: $left + $right")
            left + right
        }
        else -> throw IllegalArgumentException("Unknown expression")
    }
println(evalWithLogging(
    Sum(
        Sum(
            Num(1),
            Num(2)),
        Num(4)
    )
))