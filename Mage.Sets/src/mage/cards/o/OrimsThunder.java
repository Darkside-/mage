/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.o;

import java.util.UUID;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.SpellAbility;
import mage.abilities.condition.common.KickedCondition;
import mage.abilities.decorator.ConditionalOneShotEffect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.keyword.KickerAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.Zone;
import mage.filter.common.FilterArtifactOrEnchantmentPermanent;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPermanent;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author jeffwadsworth
 */
public class OrimsThunder extends CardImpl {

    public OrimsThunder(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{2}{W}");

        // Kicker {R}
        this.addAbility(new KickerAbility("{R}"));

        // Destroy target artifact or enchantment. If Orim's Thunder was kicked, it deals damage equal to that permanent's converted mana cost to target creature.
        this.getSpellAbility().addEffect(new OrimsThunderEffect());
        this.getSpellAbility().addTarget(new TargetPermanent(new FilterArtifactOrEnchantmentPermanent()));
        this.getSpellAbility().addEffect(new ConditionalOneShotEffect(
                new OrimsThunderEffect2(),
                KickedCondition.instance,
                "If Orim's Thunder was kicked, it deals damage equal to that permanent's converted mana cost to target creature"));
    }

    @Override
    public void adjustTargets(Ability ability, Game game) {
        if (ability instanceof SpellAbility) {
            if (KickedCondition.instance.apply(game, ability)) {
                ability.addTarget(new TargetCreaturePermanent());
            }
        }
    }

    public OrimsThunder(final OrimsThunder card) {
        super(card);
    }

    @Override
    public OrimsThunder copy() {
        return new OrimsThunder(this);
    }
}

class OrimsThunderEffect2 extends OneShotEffect {

    OrimsThunderEffect2() {
        super(Outcome.Damage);
    }

    OrimsThunderEffect2(final OrimsThunderEffect2 effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        int damage = 0;
        MageObject firstTarget = game.getLastKnownInformation(source.getFirstTarget(), Zone.BATTLEFIELD);
        Permanent secondTarget = game.getPermanent(source.getTargets().get(1).getFirstTarget());
        if (firstTarget != null) {
            damage = firstTarget.getConvertedManaCost();
        }
        boolean kicked = KickedCondition.instance.apply(game, source);
        if (kicked && secondTarget != null) {
            secondTarget.damage(damage, source.getSourceId(), game, false, true);
            return true;
        }
        return false;
    }

    @Override
    public OrimsThunderEffect2 copy() {
        return new OrimsThunderEffect2(this);
    }
}

class OrimsThunderEffect extends OneShotEffect {

    OrimsThunderEffect() {
        super(Outcome.DestroyPermanent);
        staticText = "Destroy target artifact or enchantment";
    }

    OrimsThunderEffect(final OrimsThunderEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent target = game.getPermanent(source.getFirstTarget());
        if (target != null) {
            return target.destroy(source.getSourceId(), game, false);
        }
        return false;
    }

    @Override
    public OrimsThunderEffect copy() {
        return new OrimsThunderEffect(this);
    }
}