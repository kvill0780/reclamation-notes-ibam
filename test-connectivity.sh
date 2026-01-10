#!/bin/bash

echo "🔍 Test de connectivité Frontend-Backend"
echo "========================================"

# Test du frontend
echo "1. Test du serveur frontend (port 3000)..."
if curl -s http://localhost:3000 > /dev/null; then
    echo "✅ Frontend accessible sur http://localhost:3000"
else
    echo "❌ Frontend non accessible"
fi

# Test du backend
echo "2. Test du serveur backend (port 8080)..."
if curl -s http://localhost:8080/api/auth/login -X OPTIONS > /dev/null; then
    echo "✅ Backend accessible sur http://localhost:8080"
else
    echo "❌ Backend non accessible"
fi

# Test de login
echo "3. Test de connexion avec un utilisateur de test..."
response=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST \
    -H "Content-Type: application/json" \
    -H "Origin: http://localhost:3000" \
    -d '{"email":"jean.dupont@ibam.ma","password":"password123"}' \
    http://localhost:8080/api/auth/login)

http_code=$(echo "$response" | grep "HTTP_CODE:" | cut -d: -f2)
body=$(echo "$response" | sed '/HTTP_CODE:/d')

if [ "$http_code" = "200" ]; then
    echo "✅ Connexion réussie"
    echo "📋 Réponse: $body"
else
    echo "❌ Échec de connexion (Code: $http_code)"
    echo "📋 Réponse: $body"
fi

echo ""
echo "🎯 Utilisateurs de test disponibles:"
echo "   - jean.dupont@ibam.ma (ETUDIANT)"
echo "   - marie.martin@ibam.ma (ETUDIANT)"
echo "   - ahmed.benali@ibam.ma (ENSEIGNANT)"
echo "   - fatima.alami@ibam.ma (ENSEIGNANT)"
echo "   - omar.tazi@ibam.ma (SCOLARITE)"
echo "   - rachid.bennani@ibam.ma (DA)"
echo "   Mot de passe pour tous: password123"
echo ""
echo "🌐 Accédez à l'application: http://localhost:3000"