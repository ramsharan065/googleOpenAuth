package com.test.googleAuth

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import grails.converters.JSON

import com.test.auth.User
import grails.plugin.springsecurity.SpringSecurityService;
import grails.plugin.springsecurity.annotation.Secured;

@Secured(["is_authenticated_anonymously"])
class OauthCallBackController {

	def grailsApplication
	def oauthService
	def springSecurityService

	def index() {
		render view: '/index'
	}

	def google() {
		Token googleAccessToken = (Token) session[oauthService.findSessionKeyForAccessToken('google')]
		if (googleAccessToken) {
			def googleResource = oauthService.getGoogleResource(googleAccessToken, grailsApplication.config.grails.google.api.url )
			def googleResponse = JSON.parse(googleResource?.getBody())
			println request.getHeader("Referer");
			Map data = [id: googleResponse.id,email: googleResponse.email, name: googleResponse.name, given_name: googleResponse.given_name, family_name: googleResponse.family_name,
				gender: googleResponse.gender, link: googleResponse.link]
			println data
			def username = googleResponse.email
			def user = User.getByUsername(username)
			if(user){
				springSecurityService.reauthenticate(user.username)
				render "$user.fullname is logged in "
			}else{
				render "user with ${username} is not allowed to access this system"
			}
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
