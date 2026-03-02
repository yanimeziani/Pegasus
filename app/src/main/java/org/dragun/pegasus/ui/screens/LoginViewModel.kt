package org.dragun.pegasus.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.dragun.pegasus.data.api.OpenClawApi
import org.dragun.pegasus.data.store.SessionStore
import org.dragun.pegasus.domain.model.LoginRequest
import javax.inject.Inject

data class LoginUiState(
    val apiUrl: String = "https://ops.meziani.org",
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: OpenClawApi,
    private val session: SessionStore,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    val isLoggedIn: Flow<Boolean> = session.isLoggedIn

    fun updateApiUrl(url: String) { _state.update { it.copy(apiUrl = url) } }
    fun updateUsername(u: String) { _state.update { it.copy(username = u) } }
    fun updatePassword(p: String) { _state.update { it.copy(password = p) } }

    fun login(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.username.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Username and password required") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val resp = api.login(LoginRequest(s.username, s.password))
                if (resp.isSuccessful && resp.body() != null) {
                    val body = resp.body()!!
                    session.saveSession(body.token, body.user, body.role)
                    session.saveServerConfig(s.apiUrl, "", 22, "root")
                    _state.update { it.copy(loading = false) }
                    onSuccess()
                } else {
                    _state.update {
                        it.copy(loading = false, error = "Login failed: ${resp.code()}")
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(loading = false, error = e.message ?: "Connection error")
                }
            }
        }
    }
}
