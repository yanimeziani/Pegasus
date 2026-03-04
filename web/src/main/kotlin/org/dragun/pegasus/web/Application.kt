package org.dragun.pegasus.web

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.style

fun main() {
    val host = System.getenv("PEGASUS_WEB_HOST") ?: "0.0.0.0"
    val port = (System.getenv("PEGASUS_WEB_PORT") ?: "8080").toIntOrNull() ?: 8080

    embeddedServer(Netty, host = host, port = port) {
        pegasusWebModule()
    }.start(wait = true)
}

fun Application.pegasusWebModule() {
    install(ContentNegotiation)

    routing {
        get("/health") {
            call.respondText("ok", ContentType.Text.Plain)
        }

        get("/") {
            val html = createHTML().html {
                head {
                    meta(charset = "utf-8")
                    meta(name = "viewport", content = "width=device-width, initial-scale=1")
                    style {
                        +"""
                        :root { color-scheme: light dark; }
                        * { box-sizing: border-box; }
                        body {
                          margin: 0;
                          font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, Roboto, sans-serif;
                          min-height: 100vh;
                          display: grid;
                          place-items: center;
                          background: radial-gradient(circle at 20% 10%, #d6ecff 0%, #f3f8ff 45%, #f8fbff 100%);
                          color: #10222f;
                        }
                        @media (prefers-color-scheme: dark) {
                          body {
                            background: radial-gradient(circle at 20% 10%, #1f3648 0%, #13212e 45%, #0b141d 100%);
                            color: #e2edf8;
                          }
                        }
                        .card {
                          width: min(720px, 92vw);
                          border-radius: 24px;
                          padding: 28px;
                          border: 1px solid rgba(120, 154, 176, 0.45);
                          background: color-mix(in srgb, #ffffff 88%, #dfeeff 12%);
                          box-shadow: 0 20px 60px rgba(13, 32, 48, 0.14);
                          animation: rise 420ms ease-out;
                        }
                        @media (prefers-color-scheme: dark) {
                          .card {
                            background: color-mix(in srgb, #162432 85%, #26465f 15%);
                            box-shadow: 0 22px 64px rgba(0, 0, 0, 0.35);
                          }
                        }
                        .meta { opacity: 0.8; margin-top: 8px; }
                        .row {
                          margin-top: 20px;
                          display: grid;
                          grid-template-columns: repeat(auto-fit, minmax(170px, 1fr));
                          gap: 10px;
                        }
                        .chip {
                          border-radius: 999px;
                          padding: 8px 14px;
                          border: 1px solid rgba(76, 122, 150, 0.45);
                          font-size: 14px;
                        }
                        @keyframes rise {
                          from { opacity: 0; transform: translateY(16px) scale(0.98); }
                          to { opacity: 1; transform: translateY(0) scale(1); }
                        }
                        """.trimIndent()
                    }
                    script {
                        unsafe {
                            +"""
                            setInterval(async () => {
                              const target = document.getElementById('api-health');
                              if (!target) return;
                              try {
                                const r = await fetch('/health');
                                target.textContent = r.ok ? 'web healthy' : 'web degraded';
                              } catch (_e) {
                                target.textContent = 'web unreachable';
                              }
                            }, 4000);
                            """.trimIndent()
                        }
                    }
                }
                body {
                    kotlinx.html.div(classes = "card") {
                        h1 { +"Pegasus Mission Control" }
                        kotlinx.html.p(classes = "meta") {
                            +"Kotlin web entrypoint for Cerberus operations."
                        }
                        kotlinx.html.div(classes = "row") {
                            kotlinx.html.span(classes = "chip") { +"Android App: Pegasus" }
                            kotlinx.html.span(classes = "chip") { +"Backend: Pegasus API" }
                            kotlinx.html.span(classes = "chip") { +"Runtime: Cerberus" }
                        }
                        kotlinx.html.p {
                            +"Status: "
                            kotlinx.html.strong { attributes["id"] = "api-health"; +"checking..." }
                        }
                    }
                }
            }
            call.respondText(html, ContentType.Text.Html, HttpStatusCode.OK)
        }
    }
}
