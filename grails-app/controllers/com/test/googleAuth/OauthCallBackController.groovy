package com.test.googleAuth

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import grails.converters.JSON

class OauthCallBackController {

	def oauthService

	def index() {
		render view: '/index'
	}

	def google() {
		Token googleAccessToken = (Token) session[oauthService.findSessionKeyForAccessToken('google')]
		if (googleAccessToken) {
			def googleResource = oauthService.getGoogleResource(googleAccessToken, "https://www.googleapis.com/oauth2/v1/userinfo")
			def googleResponse = JSON.parse(googleResource?.getBody())
			println request.getHeader("Referer");
			Map data = [id: googleResponse.id,email: googleResponse.email, name: googleResponse.name, given_name: googleResponse.given_name, family_name: googleResponse.family_name,
				gender: googleResponse.gender, link: googleResponse.link]
			println data
			render view: '/index', model: [provider: 'Google +', data: data]
		} else {
			flash.error = "Token not found."
			render view: '/index'
		}
	}


	def failure() {
		println "hello prkash"
		flash.error = "Error."
		render view: '/index'
	}
}
