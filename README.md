# 🧩 Puzzle Programming Language

**Version:** `0.1.3-dev`

**Puzzle** 是一门正在设计与实现中的通用编程语言，目标是在提供**清晰、可组合的抽象能力**的同时，保持**可预测的性能模型**，并最终
**直接编译为高效的原生二进制程序**。

> 把复杂问题拆解成可以拼合的“拼图”，这是 Puzzle 的名字来源，也是它的核心设计理念。

---

## ✨ 语言特性（设计目标）

Puzzle 试图在**表达力、可读性与工程可控性**之间取得平衡，融合多种现代语言设计思想。

---

## CLI 命令行工具用法

构建项目

```shell
puzzle build --path=<project-path> --debug-features=all --infos=all
```

查看版本信息

```shell
puzzle version
```

查看帮助文档

```shell
puzzle help
```

---

## 🧠 编程模型与声明体系

Puzzle 提供统一、强表达力的声明系统，**所有声明均支持在类内部嵌套声明**。

### 支持的声明类型

* **`class` 类**

    * 支持普通类、抽象类
    * 支持继承、多态
    * 支持次构造函数（Secondary Constructor / `ctor`）
    * 支持 `init` 对象初始化块

* **`fun` 函数**

    * 支持普通函数、成员函数、顶级函数
    * 支持 **多返回值（Multiple Return Values）**
    * 支持 lambda 表达式

* **`object` 单例对象**

    * 语言级单例定义
    * 支持 **带参数的单例**
    * 使用 `init ObjectName(args...)` 显式初始化
    * 使用 `init? ObjectName(args...)` 进行安全初始化（若已初始化则跳过）

* **`trait` 特征**

    * 行为抽象单位
    * 支持方法与属性声明
    * 可被类或 object 实现

* **`error` 错误**

    * 错误类型
    * 支持 `match` 获取异常
    * 支持 `|:` 忽略异常，类似: `?:` 的用法

* **`mixin` 混入**

    * 用于代码复用
    * 不参与继承层级
    * 可组合多个 mixin

* **`struct` 结构体**

    * 值语义数据结构
    * 适合轻量级数据建模

* **`enum` 枚举**

    * 支持具名枚举值
    * 可附加属性与行为

* **`annotation` 注解**

    * 编译期与语义分析期元信息
    * 可用于类型、函数、属性等声明

* **`extension` 扩展**

    * 支持扩展函数
    * 支持扩展属性
    * 支持为类型扩展特征

* **`typealias` 类型别名**

    * 提供类型语义别名
    * 支持泛型类型别名
    * 支持类型约束与类型安全隔离

---

## 👋 变量系统（Variable System）

* **普通赋值**

```puzzle
val name = getName()    // 不可变变量
var age = getAge()      // 可变变量
age++
```

* **支持解构变量**

```puzzle
val [name, age] = getInfo()                     // 强制解构
val [_, age: Int] = getInfo()                   // 支持匿名绑定
var [name: String, val age: Int] = getInfo()    // 支持默认可变性，并单独为属性设置类型和可变性
```

## 🧩 属性系统（Property System）

Puzzle 将属性视为一等语言结构，而非语法糖。

### 属性类型

* **普通属性**

* **延迟初始化属性**

    * `late var`：延迟初始化的可变属性
    * `lazy val`：惰性求值的不可变属性

* **计算属性**

    * 无存储，仅通过表达式计算

* **属性访问器**

    * 自定义 `get`
    * 自定义 `set`

---

## 🧬 泛型系统（Generics）

Puzzle 提供功能完整且偏向**静态可验证**的泛型系统。

### 泛型能力

* 支持泛型类型与泛型函数
* 支持 **多个 bound**
* 支持协变 / 逆变

    * `out T`
    * `in T`
* 支持 **默认泛型参数**
* 支持 **泛型展开（Variadic Generics）**

    * `T...`
* 支持 **reified（真泛型）**

    * 泛型信息在编译期与运行期均可感知

---

## 🧭 Context 上下文系统

Context 是 Puzzle 的核心抽象之一，用于表达**隐式依赖、作用域语义与执行环境**。

### Context 可应用于

* **函数**
* **属性**（必须为非普通属性）
* **类**
* **object 单例**
* **trait**
* **mixin**
* **次构造函数**
* **lambda 表达式**

📌 Context 用于：

* 隐式参数传递
* 作用域能力约束
* DSL 构建
* 语义环境注入

---

## 🔤 字符串能力

* **字符串模板（String Template）**
* **多行字符串模板（Multi-line String Template）**

    * 原生支持
    * 自动处理公共缩进

---

## 🏗 编译目标与架构

### 编译目标

* 直接编译为 **原生二进制程序**

### 编译器架构

* **前端（完全自研）**

    * 词法分析（Lexer）
    * 语法分析（Parser）
    * 语义分析（Semantic Analysis）
    * 类型检查（Type Checking）

* **后端**

    * 基于 **LLVM IR**
    * 利用 LLVM 的成熟优化与多平台支持能力

---

## 🚧 项目当前进度

> Puzzle 目前处于语言与编译器的早期实现阶段

* ✅ 词法分析器（Lexer）
* ✅ 语法分析器（Parser）
    * ⏳ 语义分析、类型系统、IR 设计中...

---

## 📌 项目状态说明

Puzzle 是一门**实验性语言**，语法与特性仍在快速迭代中，适合用于：

* 编程语言设计与实现研究
* 编译器前端 / LLVM 学习
* 新语言特性探索与原型验证

---

## 📜 License

（待定）
