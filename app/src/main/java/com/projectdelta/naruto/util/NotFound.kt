package com.projectdelta.naruto.util

object NotFound {

	private val TextOnlyEmoticons = listOf(
		"(='X'=)", "^o^", "(·_·)", "(>_<)",
		"(≥o≤)", "(ㆆ _ ㆆ)", "(╥﹏╥)", "<(^_^)>", "=^_^=", "(-_-;)", "(*^_^*)", "(◠﹏◠)"
	)

	fun surpriseMe() = TextOnlyEmoticons.random()

}