package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Friend
import com.example.ui.components.Card3DFriend
import com.example.util.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    friends: List<Friend>,
    onFriendClick: (Friend) -> Unit,
    onAddFriend: (name: String, username: String, bio: String, avatarUrl: String) -> Unit,
    isDark: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    var showAddModal by remember { mutableStateOf(false) }
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Sleek Minimal Header with Title and the "+" (Cross) button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Strings.chats(language),
                    color = textColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                // The Cross (+) Button as requested
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            ambientColor = if (isDark) Color.Black.copy(alpha = 0.4f) else Color(0xFF0284C7).copy(alpha = 0.2f)
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (isDark) listOf(Color(0xFF38BDF8), Color(0xFF818CF8))
                                else listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                            )
                        )
                        .border(1.2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                        .clickable { showAddModal = true }
                        .testTag("add_user_cross_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar amigo",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Grid of 3D Square Cards with Full-Bleed Profile Photos & Slim Name Badge
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 8.dp,
                    bottom = 110.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("chats_3d_grid")
            ) {
                items(
                    items = friends,
                    key = { it.id }
                ) { friend ->
                    Card3DFriend(
                        friend = friend,
                        onClick = { onFriendClick(friend) },
                        isDark = isDark,
                        unreadCount = if (friend.id == "friend_1") 1 else 0
                    )
                }
            }
        }

        // Modal Bottom Sheet when clicking "+"
        if (showAddModal) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var newName by remember { mutableStateOf("") }
            var newUsername by remember { mutableStateOf("") }
            var newBio by remember { mutableStateOf("") }
            var selectedAvatar by remember {
                mutableStateOf("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop&q=80")
            }

            ModalBottomSheet(
                onDismissRequest = { showAddModal = false },
                sheetState = sheetState,
                containerColor = if (isDark) Color(0xFF0B132B) else Color(0xFFF8FAFC),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Strings.addFriend(language),
                            color = textColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showAddModal = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = subTextColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Form to add a new friend
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre del amigo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("Usuario (@amigo)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (newName.isNotBlank()) {
                                onAddFriend(newName, newUsername.ifBlank { newName.lowercase().replace(" ", "_") }, newBio, selectedAvatar)
                                showAddModal = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                        )
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agregar a Vibe Privado", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Existing friends in square card format with mini name badge as requested
                    Text(
                        text = Strings.existingFriends(language),
                        color = textColor,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(friends) { friend ->
                            Box(modifier = Modifier.width(110.dp)) {
                                Card3DFriend(
                                    friend = friend,
                                    onClick = {
                                        showAddModal = false
                                        onFriendClick(friend)
                                    },
                                    isDark = isDark
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
